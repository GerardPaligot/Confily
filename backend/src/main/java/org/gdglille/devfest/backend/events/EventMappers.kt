@file:Suppress("TooManyFunctions")

package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.internals.slug
import org.gdglille.devfest.models.Acronym
import org.gdglille.devfest.models.Address
import org.gdglille.devfest.models.EventItemList
import org.gdglille.devfest.models.EventLunchMenu
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.EventV2
import org.gdglille.devfest.models.FeaturesActivated
import org.gdglille.devfest.models.QuestionAndResponse
import org.gdglille.devfest.models.QuestionAndResponseAction
import org.gdglille.devfest.models.inputs.AcronymInput
import org.gdglille.devfest.models.inputs.BilletWebConfigInput
import org.gdglille.devfest.models.inputs.CategoryInput
import org.gdglille.devfest.models.inputs.ConferenceHallConfigInput
import org.gdglille.devfest.models.inputs.CreatingEventInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import org.gdglille.devfest.models.inputs.QuestionAndResponseActionInput
import org.gdglille.devfest.models.inputs.QuestionAndResponseInput
import org.gdglille.devfest.models.inputs.WldConfigInput
import java.util.UUID

fun AcronymDb.convertToModel() = Acronym(
    key = key,
    value = value
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
    actions = this.actions.map { it.convertToModel() },
    acronyms = this.acronyms.map { it.convertToModel() }
)

fun LunchMenuDb.convertToModel() = EventLunchMenu(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)

fun AddressDb.convertToModel() = Address(
    formatted = this.formatted,
    address = this.address,
    country = this.country,
    countryCode = this.countryCode,
    city = this.city,
    lat = this.lat,
    lng = this.lng
)

fun EventDb.convertToFeaturesActivatedModel(hasPartnerList: Boolean) = FeaturesActivated(
    hasNetworking = features.hasNetworking,
    hasSpeakerList = !features.hasNetworking,
    hasPartnerList = hasPartnerList,
    hasMenus = menus.isNotEmpty(),
    hasQAndA = qanda.isNotEmpty(),
    hasBilletWebTicket = billetWebConfig != null
)

fun EventDb.convertToModel(partners: EventPartners) = org.gdglille.devfest.models.Event(
    id = this.slugId,
    name = this.name,
    address = this.address.convertToModel(),
    startDate = this.startDate,
    endDate = this.endDate,
    partners = partners,
    menus = menus.map { it.convertToModel() },
    qanda = qanda.map { it.convertToModel() },
    coc = coc,
    features = this.convertToFeaturesActivatedModel(
        partners.golds.isNotEmpty() ||
            partners.silvers.isNotEmpty() ||
            partners.bronzes.isNotEmpty() ||
            partners.others.isNotEmpty()
    ),
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun EventDb.convertToEventItemList() = EventItemList(
    id = this.slugId,
    name = this.name,
    startDate = this.startDate,
    endDate = this.endDate
)

fun EventDb.convertToModelV2(hasPartnerList: Boolean) = EventV2(
    id = this.slugId,
    name = this.name,
    address = this.address.convertToModel(),
    startDate = this.startDate,
    endDate = this.endDate,
    menus = menus.map { it.convertToModel() },
    qanda = qanda.map { it.convertToModel() },
    coc = coc,
    openfeedbackProjectId = this.openFeedbackId,
    features = this.convertToFeaturesActivatedModel(hasPartnerList),
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun AcronymInput.convertToDb() = AcronymDb(
    key = key,
    value = value
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
    actions = actions.mapIndexed { index, it -> it.convertToDb(index) },
    acronyms = acronyms.map { it.convertToDb() }
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

fun ConferenceHallConfigInput.convertToDb() = ConferenceHallConfigurationDb(
    eventId = eventId,
    apiKey = apiKey
)

fun BilletWebConfigInput.convertToDb() = BilletWebConfigurationDb(
    eventId = eventId,
    userId = userId,
    apiKey = apiKey
)

fun WldConfigInput.convertToDb() = WldConfigurationDb(
    appId = appId,
    apiKey = apiKey
)

fun EventInput.convertToDb(event: EventDb, openFeedbackId: String?, apiKey: String) = EventDb(
    slugId = event.name.slug(),
    year = event.year,
    openFeedbackId = openFeedbackId ?: event.openFeedbackId,
    conferenceHallConfig = this.conferenceHallConfigInput?.convertToDb(),
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    wldConfig = this.wldConfig?.convertToDb(),
    apiKey = apiKey,
    name = this.name,
    address = AddressDb(
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
    sponsoringTypes = this.sponsoringTypes,
    formats = this.formats,
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun CreatingEventInput.convertToDb(addressDb: AddressDb) = EventDb(
    slugId = name.slug(),
    year = year,
    openFeedbackId = openFeedbackId,
    conferenceHallConfig = this.conferenceHallConfigInput?.convertToDb(),
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    wldConfig = this.wldConfig?.convertToDb(),
    apiKey = UUID.randomUUID().toString(),
    name = this.name,
    address = addressDb,
    startDate = this.startDate,
    endDate = this.endDate,
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)
