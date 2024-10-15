package com.paligot.confily.core.agenda

import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.QuestionAndResponse

interface AgendaDao {
    fun saveAgenda(eventId: String, agenda: AgendaV4)
    fun insertEvent(event: EventV3, qAndA: List<QuestionAndResponse>)
    fun insertPartners(eventId: String, partners: Map<String, List<PartnerV2>>)
}
