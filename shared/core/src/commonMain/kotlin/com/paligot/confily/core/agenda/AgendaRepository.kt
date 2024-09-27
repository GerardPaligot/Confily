package com.paligot.confily.core.agenda

import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.api.exceptions.AgendaNotModifiedException
import com.paligot.confily.core.db.ConferenceSettings
import com.paligot.confily.models.ui.ScaffoldConfigUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

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
            settings: ConferenceSettings,
            agendaDao: AgendaDao,
            featuresDao: FeaturesActivatedDao
        ): AgendaRepository = AgendaRepositoryImpl(api, settings, agendaDao, featuresDao)
    }
}

@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class AgendaRepositoryImpl(
    private val api: ConferenceApi,
    private val settings: ConferenceSettings,
    private val agendaDao: AgendaDao,
    private val featuresDao: FeaturesActivatedDao
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val eventId = settings.getEventId()
        val etag = settings.lastEtag(eventId)
        try {
            val (newEtag, agenda) = api.fetchAgenda(eventId, etag)
            agendaDao.saveAgenda(eventId, agenda)
            settings.updateEtag(eventId, newEtag)
        } catch (ex: AgendaNotModifiedException) {
            ex.printStackTrace()
        }
        val event = api.fetchEvent(eventId)
        val qanda = api.fetchQAndA(eventId)
        val partners = api.fetchPartners(eventId)
        agendaDao.insertEvent(event, qanda)
        agendaDao.insertPartners(eventId, partners)
    }

    override fun scaffoldConfig(): Flow<ScaffoldConfigUi> = settings.fetchEventIdOrNull()
        .flatMapConcat { featuresDao.fetchScaffoldConfig(it) }
}
