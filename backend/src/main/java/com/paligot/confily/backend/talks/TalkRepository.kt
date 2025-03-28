package com.paligot.confily.backend.talks

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToDb
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.models.inputs.TalkInput
import com.paligot.confily.models.inputs.TalkVerbatimInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class TalkRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val driveDataSource: DriveDataSource
) {
    suspend fun list(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val talks = sessionDao.getAllTalkSessions(eventId)
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

    suspend fun create(eventId: String, talkInput: TalkInput) = coroutineScope {
        val event = eventDao.get(eventId)
        val talkDb = talkInput.convertToDb()
        val id = sessionDao.createOrUpdate(eventId, talkDb)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope id
    }

    suspend fun get(eventId: String, talkId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val talk = sessionDao.getTalkSession(eventId, talkId)
            ?: throw NotFoundException("Talk $talkId Not Found")
        val categoryDb = categoryDao.get(eventId, talk.category)
        val formatDb = formatDao.get(eventId, talk.format)
        return@coroutineScope talk.convertToModel(
            speakerDao.getByIds(eventId, talk.speakerIds),
            categoryDb,
            formatDb,
            eventDb
        )
    }

    suspend fun update(eventId: String, talkId: String, talkInput: TalkInput) =
        coroutineScope {
            val event = eventDao.get(eventId)
            sessionDao.createOrUpdate(eventId, talkInput.convertToDb(id = talkId))
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope talkId
        }

    suspend fun verbatim(eventId: String, verbatim: TalkVerbatimInput) = coroutineScope {
        val driveId = driveDataSource.findDriveByName(verbatim.driveName)
            ?: throw NotFoundException("Drive ${verbatim.driveName} doesn't exist")
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId = driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
            ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val talks = sessionDao.getAllTalkSessions(eventId)
        val speakers = speakerDao.getAll(eventId)
        val asyncVerbatims = talks.map { talkDb ->
            async {
                verbatimByTalk(
                    targetFolderId = targetFolderId,
                    talkId = talkDb.id,
                    talkTitle = talkDb.title,
                    speakers = speakers.filter { talkDb.speakerIds.contains(it.id) },
                    driveId = driveId,
                    templateId = templateId
                )
            }
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
        val eventFolderId = driveDataSource.findFolderByName(verbatim.eventFolder, null, driveId)
            ?: throw NotFoundException("Folder ${verbatim.eventFolder} doesn't exist")
        val targetFolderId = driveDataSource.findFolderByName(verbatim.targetFolder, eventFolderId, driveId)
            ?: throw NotFoundException("Folder ${verbatim.targetFolder} doesn't exist")
        val templateId = driveDataSource.findFileByName(verbatim.templateName, null, driveId)
            ?: throw NotFoundException("File ${verbatim.templateName} doesn't exist")
        val talkDb = sessionDao.getTalkSession(eventId, talkId)
            ?: throw NotFoundException("Talk $talkId doesn't exist")
        val speakers = speakerDao.getByIds(eventId, talkDb.speakerIds)
        return@coroutineScope verbatimByTalk(targetFolderId, talkId, talkDb.title, speakers, driveId, templateId)
    }

    @Suppress("LongParameterList")
    private suspend fun verbatimByTalk(
        targetFolderId: String,
        talkId: String,
        talkTitle: String,
        speakers: List<SpeakerDb>,
        driveId: String,
        templateId: String
    ) = coroutineScope {
        val emailSpeakers = speakers.filter { it.email != null }.map { it.email!! }
        if (emailSpeakers.isEmpty()) {
            throw NotFoundException("No speakers in talk $talkId")
        }
        val folderId = driveDataSource.createFolder(talkTitle, targetFolderId, driveId)
        val fileId = driveDataSource.copyFile("Verbatims", templateId, driveId)
        driveDataSource.moveFile(fileId, folderId)
        emailSpeakers.forEach { driveDataSource.grantPermission(fileId = folderId, email = it) }
        return@coroutineScope folderId
    }
}
