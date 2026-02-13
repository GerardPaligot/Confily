package com.paligot.confily.core.models.factory

import com.paligot.confily.models.Address
import com.paligot.confily.models.CodeOfConduct
import com.paligot.confily.models.EventContact
import com.paligot.confily.models.EventLunchMenu
import com.paligot.confily.models.EventV5
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.QAndA
import com.paligot.confily.models.ThirdParty
import kotlinx.datetime.Instant

fun EventV5.Companion.builder(): EventBuilder = EventBuilder()

@Suppress("TooManyFunctions")
class EventBuilder {
    private var id: String = ""
    private var name: String = ""
    private var address: Address = Address(
        formatted = emptyList(),
        address = "",
        country = "",
        countryCode = "",
        city = "",
        lat = 0.0,
        lng = 0.0
    )
    private var startDate: String = ""
    private var endDate: String = ""
    private var menus: List<EventLunchMenu> = emptyList()
    private var coc: String = ""
    private var openfeedbackProjectId: String? = null
    private var features: FeaturesActivated = FeaturesActivated(
        hasNetworking = false,
        hasSpeakerList = false,
        hasPartnerList = false,
        hasMenus = false,
        hasQAndA = false,
        hasBilletWebTicket = false
    )
    private var contactPhone: String? = null
    private var contactEmail: String = ""
    private var faqLink: String? = ""
    private var codeOfConductLink: String? = ""
    private var updatedAt: Long = 0L

    fun id(id: String) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun address(address: Address) = apply { this.address = address }
    fun startDate(startDate: Instant) = apply { this.startDate = startDate.formatDateISO() }
    fun endDate(endDate: Instant) = apply { this.endDate = endDate.formatDateISO() }
    fun menus(menus: List<EventLunchMenu>) = apply { this.menus = menus }
    fun coc(coc: String) = apply { this.coc = coc }
    fun openfeedbackProjectId(openfeedbackProjectId: String?) =
        apply { this.openfeedbackProjectId = openfeedbackProjectId }

    fun features(features: FeaturesActivated) = apply { this.features = features }
    fun contactPhone(contactPhone: String?) = apply { this.contactPhone = contactPhone }
    fun contactEmail(contactEmail: String) = apply { this.contactEmail = contactEmail }
    fun faqLink(faqLink: String?) = apply { this.faqLink = faqLink }
    fun codeOfConductLink(codeOfConductLink: String?) =
        apply { this.codeOfConductLink = codeOfConductLink }

    fun updatedAt(updatedAt: Long) = apply { this.updatedAt = updatedAt }

    fun build(): EventV5 = EventV5(
        id = id,
        name = name,
        address = address,
        startDate = startDate,
        endDate = endDate,
        menus = menus,
        coc = CodeOfConduct(
            content = coc,
            link = codeOfConductLink
        ),
        qanda = QAndA(
            content = emptyMap(),
            link = faqLink
        ),
        features = features,
        contact = EventContact(
            phone = contactPhone,
            email = contactEmail,
            socials = emptyList()
        ),
        team = emptyMap(),
        maps = emptyList(),
        thirdParty = ThirdParty(
            openfeedbackProjectId = openfeedbackProjectId
        ),
        updatedAt = updatedAt
    )
}
