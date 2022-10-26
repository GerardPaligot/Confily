package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.internals.slug
import org.gdglille.devfest.backend.network.conferencehall.Event
import org.gdglille.devfest.backend.partners.PartnerDb
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.models.EventAddress
import org.gdglille.devfest.models.EventLunchMenu
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.FeaturesActivated
import org.gdglille.devfest.models.QuestionAndResponse
import org.gdglille.devfest.models.QuestionAndResponseAction
import org.gdglille.devfest.models.inputs.BilletWebConfigInput
import org.gdglille.devfest.models.inputs.CategoryInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import org.gdglille.devfest.models.inputs.QuestionAndResponseActionInput
import org.gdglille.devfest.models.inputs.QuestionAndResponseInput

fun Event.convertToDb(year: String, eventId: String, apiKey: String) = EventDb(
    slugId = this.name.slug(),
    year = year,
    conferenceHallId = eventId,
    apiKey = apiKey,
    name = this.name,
    address = EventAddressDb(
        formatted = this.address.formattedAddress.split(",").map { it.trim() },
        address = this.address.formattedAddress,
        country = this.address.country.longName,
        countryCode = this.address.country.shortName,
        city = this.address.locality.longName,
        lat = this.address.latLng.lat,
        lng = this.address.latLng.lng
    ),
    startDate = this.conferenceDates.start,
    endDate = this.conferenceDates.end
)

fun QuestionAndResponseActionDb.convertToModel() = QuestionAndResponseAction(
    order = order,
    label = label,
    url = url
)

fun QuestionAndResponseDb.convertToModel() = QuestionAndResponse(
    order = order,
    question = question,
    response = response,
    actions = this.actions.map { it.convertToModel() }
)

fun LunchMenuDb.convertToModel() = EventLunchMenu(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)

fun EventDb.convertToModel(
    golds: List<PartnerDb>,
    silvers: List<PartnerDb>,
    bronzes: List<PartnerDb>,
    others: List<PartnerDb>
) = org.gdglille.devfest.models.Event(
    id = this.slugId,
    name = this.name,
    address = EventAddress(
        formatted = this.address.formatted,
        address = this.address.address,
        country = this.address.country,
        countryCode = this.address.countryCode,
        city = this.address.city,
        lat = this.address.lat,
        lng = this.address.lng
    ),
    startDate = this.startDate,
    endDate = this.endDate,
    partners = EventPartners(
        golds = golds.map { it.convertToModel() },
        silvers = silvers.map { it.convertToModel() },
        bronzes = bronzes.map { it.convertToModel() },
        others = others.map { it.convertToModel() }
    ),
    menus = menus.map { it.convertToModel() },
    qanda = qanda.map { it.convertToModel() },
    coc = coc,
    features = FeaturesActivated(
        hasNetworking = features.hasNetworking,
        hasSpeakerList = !features.hasNetworking,
        hasPartnerList = golds.isNotEmpty() || silvers.isNotEmpty() || bronzes.isNotEmpty() || others.isNotEmpty(),
        hasMenus = menus.isNotEmpty(),
        hasQAndA = qanda.isNotEmpty(),
        hasBilletWebTicket = billetWebConfig != null
    ),
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun QuestionAndResponseActionInput.convertToDb(order: Int) = QuestionAndResponseActionDb(
    order = order,
    label = label,
    url = url
)

fun QuestionAndResponseInput.convertToDb(order: Int) = QuestionAndResponseDb(
    order = order,
    question = question,
    response = response,
    actions = actions.mapIndexed { index, it -> it.convertToDb(index) }
)

fun LunchMenuInput.convertToDb() = LunchMenuDb(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)

fun CategoryInput.convertToDb() = CategoryDb(
    name = name,
    color = color,
    icon = icon
)

fun BilletWebConfigInput.convertToDb() = BilletWebConfigurationDb(
    eventId = eventId,
    userId = userId,
    apiKey = apiKey
)

fun EventInput.convertToDb(event: EventDb, openFeedbackId: String?, apiKey: String) = EventDb(
    slugId = event.name.slug(),
    year = event.year,
    conferenceHallId = event.conferenceHallId,
    openFeedbackId = openFeedbackId ?: event.openFeedbackId,
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    apiKey = apiKey,
    name = this.name,
    address = EventAddressDb(
        formatted = this.address.address.split(",").map { it.trim() },
        address = this.address.address,
        country = this.address.country,
        countryCode = this.address.countryCode,
        city = this.address.city,
        lat = this.address.lat,
        lng = this.address.lng
    ),
    menus = event.menus,
    qanda = event.qanda,
    coc = event.coc,
    startDate = this.startDate,
    endDate = this.endDate,
    formats = this.formats,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)
