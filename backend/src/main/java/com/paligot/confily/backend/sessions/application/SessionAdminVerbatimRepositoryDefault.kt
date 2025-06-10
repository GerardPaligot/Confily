package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.TalkSessionEntity
import com.paligot.confily.backend.sessions.domain.SessionAdminVerbatimRepository
import com.paligot.confily.models.inputs.TalkVerbatimInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SessionAdminVerbatimRepositoryDefault(
    private val speakerFirestore: SpeakerFirestore,
    private val sessionFirestore: SessionFirestore,
    private val driveDataSource: DriveDataSource
) : SessionAdminVerbatimRepository {
    override suspend fun create(eventId: String, verbatim: TalkVerbatimInput): List<String> = coroutineScope {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val talks = sessionFirestore.getAllTalkSessions(eventId)
        val asyncVerbatims = talks.map { talkDb ->
            async {
                createVerbatimTalkFolder(
                    eventId = eventId,
                    talkDb = talkDb,
                    targetFolderId = targetFolderId,
                    driveId = driveId,
                    templateId = templateId
                )
            }
        }
        return@coroutineScope asyncVerbatims.awaitAll()
    }

    override suspend fun create(eventId: String, talkId: String, verbatim: TalkVerbatimInput): String {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val talkDb = sessionFirestore.getTalkSession(eventId, talkId)
            ?: throw NotFoundException("Talk $talkId doesn't exist")
        return createVerbatimTalkFolder(
            eventId = eventId,
            talkDb = talkDb,
            targetFolderId = targetFolderId,
            driveId = driveId,
            templateId = templateId
        )
    }

    private fun createVerbatimTalkFolder(
        eventId: String,
        talkDb: TalkSessionEntity,
        targetFolderId: String,
        driveId: String,
        templateId: String
    ): String {
        if (talkDb.driveFolderId != null) {
            val talkFolder = driveDataSource.findFolderById(
                id = talkDb.driveFolderId,
                name = talkDb.title,
                parentId = targetFolderId,
                driveId = driveId
            )
            if (talkFolder != null) {
                return talkFolder
            }
        }
        val folderId = driveDataSource.createFolder(talkDb.title, targetFolderId, driveId)
        val fileId = driveDataSource.copyFile("Verbatims", templateId, driveId)
        driveDataSource.moveFile(fileId, folderId)
        sessionFirestore.update(eventId, talkDb.copy(driveFolderId = folderId))
        return folderId
    }

    override suspend fun grantPermissions(eventId: String, verbatim: TalkVerbatimInput): Unit = coroutineScope {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val talks = sessionFirestore.getAllTalkSessions(eventId)
        val speakers = speakerFirestore.getAll(eventId)
        talks
            .filter { it.driveFolderId != null }
            .map { talkDb ->
                async {
                    val speakerEmails = speakers
                        .filter { talkDb.speakerIds.contains(it.id) }
                        .filter { it.email != null }
                        .map { it.email!! }
                    val folderId = grantPermission(
                        driveFolderId = talkDb.driveFolderId!!,
                        folderName = talkDb.title,
                        emails = speakerEmails,
                        targetFolderId = targetFolderId,
                        driveId = driveId
                    )
                    if (folderId == null) {
                        sessionFirestore.update(eventId, talkDb.copy(driveFolderId = null))
                    }
                }
            }.awaitAll()
    }

    override suspend fun grantPermissionByTalk(eventId: String, talkId: String, verbatim: TalkVerbatimInput): String {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val talkDb = sessionFirestore.getTalkSession(eventId = eventId, id = talkId)
            ?: throw NotFoundException("Talk $talkId doesn't exist")
        val driveFolderId = talkDb.driveFolderId
            ?: throw NotFoundException("Talk $talkId doesn't have a verbatim folder")
        val speakerEmails = speakerFirestore.getByIds(eventId, talkDb.speakerIds)
            .filter { it.email != null }
            .map { it.email!! }
        val folderId = grantPermission(
            driveFolderId = driveFolderId,
            folderName = talkDb.title,
            emails = speakerEmails,
            targetFolderId = targetFolderId,
            driveId = driveId
        )
        if (folderId == null) {
            sessionFirestore.update(eventId, talkDb.copy(driveFolderId = null))
            throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        }
        return folderId
    }

    private fun grantPermission(
        driveFolderId: String,
        folderName: String,
        emails: List<String>,
        targetFolderId: String,
        driveId: String
    ): String? {
        val folderId = driveDataSource.findFolderById(
            id = driveFolderId,
            name = folderName,
            parentId = targetFolderId,
            driveId = driveId
        )
        if (folderId != null) {
            emails.forEach {
                val hasPermission = driveDataSource.hasPermission(fileId = folderId, email = it)
                if (hasPermission.not()) {
                    driveDataSource.grantPermission(fileId = folderId, email = it)
                }
            }
        }
        return folderId
    }
}
