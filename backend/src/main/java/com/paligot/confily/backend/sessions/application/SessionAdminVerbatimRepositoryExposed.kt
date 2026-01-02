package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.sessions.domain.SessionAdminVerbatimRepository
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.models.inputs.TalkVerbatimInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SessionAdminVerbatimRepositoryExposed(
    private val database: Database,
    private val driveDataSource: DriveDataSource
) : SessionAdminVerbatimRepository {
    override suspend fun create(eventId: String, verbatim: TalkVerbatimInput): List<String> = coroutineScope {
        val eventUuid = UUID.fromString(eventId)
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val sessions = transaction(db = database) {
            SessionEntity.findByEvent(eventUuid)
        }
        val asyncVerbatims = sessions.map { session ->
            async {
                val folderId = createVerbatimTalkFolder(
                    sessionEntity = session,
                    targetFolderId = targetFolderId,
                    driveId = driveId,
                    templateId = templateId
                )
                transaction(db = database) { session.driveFolderId = folderId }
                folderId
            }
        }
        return@coroutineScope asyncVerbatims.awaitAll()
    }

    override suspend fun create(eventId: String, talkId: String, verbatim: TalkVerbatimInput): String {
        val eventUuid = UUID.fromString(eventId)
        val talkUuid = UUID.fromString(talkId)
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val sessionEntity = transaction(db = database) {
            SessionEntity
                .findById(eventUuid, talkUuid)
                ?: throw NotFoundException("Talk $talkId doesn't exist")
        }
        val folderId = createVerbatimTalkFolder(
            sessionEntity = sessionEntity,
            targetFolderId = targetFolderId,
            driveId = driveId,
            templateId = templateId
        )
        transaction(db = database) { sessionEntity.driveFolderId = folderId }
        return folderId
    }

    override suspend fun grantPermissions(eventId: String, verbatim: TalkVerbatimInput): Unit = coroutineScope {
        val eventUuid = UUID.fromString(eventId)
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val sessions = transaction(db = database) {
            SessionEntity
                .findByDriveFolderId(eventId = eventUuid, driveFolderId = null)
                .toList()
        }
        val speakers = transaction(db = database) {
            SpeakerEntity
                .findByEvent(eventUuid)
                .toList()
        }
        sessions.map { session ->
            val speakerIds = transaction(db = database) {
                SessionSpeakersTable.speakerIds(session.id.value)
            }
            async {
                val speakerEmails = speakers
                    .filter { speakerIds.contains(it.id.value) }
                    .filter { it.email != null }
                    .map { it.email!! }
                val folderId = grantPermission(
                    driveFolderId = session.driveFolderId!!,
                    folderName = session.title,
                    emails = speakerEmails,
                    targetFolderId = targetFolderId,
                    driveId = driveId
                )
                if (folderId == null) {
                    transaction(db = database) {
                        session.driveFolderId = null
                    }
                }
            }
        }.awaitAll()
    }

    override suspend fun grantPermissionByTalk(
        eventId: String,
        talkId: String,
        verbatim: TalkVerbatimInput
    ): String {
        val eventUuid = UUID.fromString(eventId)
        val talkUuid = UUID.fromString(talkId)
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId =
            driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
                ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val sessionEntity = transaction(db = database) {
            SessionEntity
                .findById(eventUuid, talkUuid)
                ?: throw NotFoundException("Talk $talkId doesn't exist")
        }
        val driveFolderId = sessionEntity.driveFolderId
            ?: throw NotFoundException("Talk $talkId doesn't have a verbatim folder")
        val speakerEmails = transaction(db = database) {
            val speakersIds = SessionSpeakersTable.speakerIds(sessionEntity.id.value)
            SpeakerEntity
                .findByIdsWithEmail(eventUuid, speakersIds)
                .toList()
                .map { it.email!! }
        }
        val folderId = grantPermission(
            driveFolderId = driveFolderId,
            folderName = sessionEntity.title,
            emails = speakerEmails,
            targetFolderId = targetFolderId,
            driveId = driveId
        )
        if (folderId == null) {
            transaction(db = database) {
                sessionEntity.driveFolderId = null
            }
            throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        }
        return folderId
    }

    private fun createVerbatimTalkFolder(
        sessionEntity: SessionEntity,
        targetFolderId: String,
        driveId: String,
        templateId: String
    ): String {
        val driveFolderId = sessionEntity.driveFolderId
        if (driveFolderId != null) {
            val talkFolder = driveDataSource.findFolderById(
                id = driveFolderId,
                name = sessionEntity.title,
                parentId = targetFolderId,
                driveId = driveId
            )
            if (talkFolder != null) {
                return talkFolder
            }
        }
        val folderId = driveDataSource.createFolder(sessionEntity.title, targetFolderId, driveId)
        val fileId = driveDataSource.copyFile("Verbatims", templateId, driveId)
        driveDataSource.moveFile(fileId, folderId)
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
