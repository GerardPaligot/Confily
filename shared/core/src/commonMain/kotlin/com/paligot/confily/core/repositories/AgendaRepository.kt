package com.paligot.confily.core.repositories

import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.api.exceptions.AgendaNotModifiedException
import com.paligot.confily.core.database.AgendaDao
import com.paligot.confily.core.database.FeaturesActivatedDao
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.models.ui.ScaffoldConfigUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {
    @NativeCoroutines
    suspend fun fetchAndStoreAgenda()

    @NativeCoroutines
    fun scaffoldConfig(): Flow<ScaffoldConfigUi>

    @FlowPreview
    @ExperimentalSettingsApi
    @ExperimentalCoroutinesApi
    object Factory {
        fun create(
            api: ConferenceApi,
            agendaDao: AgendaDao,
            eventDao: EventDao,
            featuresDao: FeaturesActivatedDao
        ): AgendaRepository = AgendaRepositoryImpl(api, agendaDao, eventDao, featuresDao)
    }
}

@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class AgendaRepositoryImpl(
    private val api: ConferenceApi,
    private val agendaDao: AgendaDao,
    private val eventDao: EventDao,
    private val featuresDao: FeaturesActivatedDao
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val eventId = eventDao.getEventId()
        val etag = agendaDao.lastEtag(eventId)
        try {
            val (newEtag, agenda) = api.fetchAgenda(eventId, etag)
            agendaDao.saveAgenda(eventId, agenda)
            agendaDao.updateEtag(eventId, newEtag)
        } catch (ex: AgendaNotModifiedException) {
            ex.printStackTrace()
        }
        val event = api.fetchEvent(eventId)
        val qanda = api.fetchQAndA(eventId)
        val partners = api.fetchPartners(eventId)
        agendaDao.insertEvent(event, qanda)
        agendaDao.insertPartners(eventId, partners)
    }

    override fun scaffoldConfig(): Flow<ScaffoldConfigUi> = featuresDao.fetchFeatures()
}
