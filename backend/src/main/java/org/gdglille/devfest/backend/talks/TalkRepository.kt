package org.gdglille.devfest.backend.talks

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.internals.helpers.drive.GoogleDriveDataSource
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.models.inputs.TalkInput
import org.gdglille.devfest.models.inputs.TalkVerbatimInput

class TalkRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val driveDataSource: GoogleDriveDataSource
) {
    suspend fun list(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val talks = talkDao.getAll(eventId)
        val speakers = speakerDao.getAll(eventId)
        val categories = categoryDao.getAll(eventId)
        val formats = formatDao.getAll(eventId)
        val asyncItems = talks.map { talkDb ->
            async {
                talkDb.convertToModel(
                    speakers.filter { talkDb.speakerIds.contains(it.id) },
                    categories.find { it.id == talkDb.category },
                    formats.find { it.id == talkDb.format },
                    eventDb
                )
            }
        }
        return@coroutineScope asyncItems.awaitAll()
    }

    suspend fun create(eventId: String, apiKey: String, talkInput: TalkInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val talkDb = talkInput.convertToDb()
        val id = talkDao.createOrUpdate(eventId, talkDb)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope id
    }

    suspend fun get(eventId: String, talkId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val talk = talkDao.get(eventId, talkId) ?: throw NotFoundException("Talk $talkId Not Found")
        val categoryDb = categoryDao.get(eventId, talk.category)
        val formatDb = formatDao.get(eventId, talk.format)
        return@coroutineScope talk.convertToModel(
            speakerDao.getByIds(eventId, talk.speakerIds),
            categoryDb,
            formatDb,
            eventDb
        )
    }

    suspend fun update(eventId: String, apiKey: String, talkId: String, talkInput: TalkInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            talkDao.createOrUpdate(eventId, talkInput.convertToDb(id = talkId))
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope talkId
        }

    suspend fun verbatim(eventId: String, verbatim: TalkVerbatimInput) = coroutineScope {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(driveId, null, verbatim.eventFolder)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId = driveDataSource.findFolderByName(driveId, eventFolderId, verbatim.targetFolder)
            ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(driveId, null, verbatim.templateName)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val talks = talkDao.getAll(eventId)
        val asyncVerbatims = talks.map { talkDb ->
            async { verbatimByTalk(eventId, talkDb.id, driveId, targetFolderId, templateId) }
        }
        return@coroutineScope asyncVerbatims.awaitAll()
    }

    suspend fun verbatim(
        eventId: String,
        talkId: String,
        verbatim: TalkVerbatimInput
    ) = coroutineScope {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(driveId, null, verbatim.eventFolder)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId = driveDataSource.findFolderByName(driveId, eventFolderId, verbatim.targetFolder)
            ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(driveId, null, verbatim.templateName)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        return@coroutineScope verbatimByTalk(eventId, talkId, driveId, targetFolderId, templateId)
    }

    private suspend fun verbatimByTalk(
        eventId: String,
        talkId: String,
        driveId: String,
        targetFolderId: String,
        templateId: String
    ) = coroutineScope {
        val talkDb = talkDao.get(eventId, talkId)
            ?: throw NotFoundException("Talk $talkId doesn't exist")
        val emailSpeakers = speakerDao.getByIds(eventId, talkDb.speakerIds)
            .filter { it.email != null }
            .map { it.email!! }
        if (emailSpeakers.isEmpty()) {
            throw NotFoundException("No speakers in talk $talkId")
        }
        val talkFolderId = driveDataSource.findFolderByName(driveId, targetFolderId, talkDb.title)
        if (talkFolderId != null) {
            return@coroutineScope talkFolderId
        }
        val fileId = driveDataSource.copyFile(driveId, templateId, "Verbatims")
        val folderId = driveDataSource.createFolder(driveId, targetFolderId, talkDb.title)
        driveDataSource.moveFile(driveId, fileId, folderId)
        emailSpeakers.forEach { driveDataSource.grantPermission(fileId = folderId, email = it) }
        return@coroutineScope folderId
    }
}
