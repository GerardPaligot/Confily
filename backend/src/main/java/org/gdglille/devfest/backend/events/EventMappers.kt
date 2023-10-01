@file:Suppress("TooManyFunctions")

package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.internals.slug
import org.gdglille.devfest.backend.qanda.QAndADb
import org.gdglille.devfest.backend.qanda.convertToModel
import org.gdglille.devfest.models.Address
import org.gdglille.devfest.models.EventItemList
import org.gdglille.devfest.models.EventLunchMenu
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.EventV2
import org.gdglille.devfest.models.EventV3
import org.gdglille.devfest.models.FeaturesActivated
import org.gdglille.devfest.models.inputs.BilletWebConfigInput
import org.gdglille.devfest.models.inputs.ConferenceHallConfigInput
import org.gdglille.devfest.models.inputs.CreatingEventInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import org.gdglille.devfest.models.inputs.WldConfigInput
import java.util.UUID

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

fun EventDb.convertToFeaturesActivatedModel(hasPartnerList: Boolean, hasQandA: Boolean) = FeaturesActivated(
    hasNetworking = features.hasNetworking,
    hasSpeakerList = !features.hasNetworking,
    hasPartnerList = hasPartnerList,
    hasMenus = menus.isNotEmpty(),
    hasQAndA = hasQandA,
    hasBilletWebTicket = billetWebConfig != null
)

fun EventDb.convertToModel(partners: EventPartners, qanda: List<QAndADb>) = org.gdglille.devfest.models.Event(
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
            partners.others.isNotEmpty(),
        qanda.isNotEmpty()
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

fun EventDb.convertToModelV2(hasPartnerList: Boolean, qanda: List<QAndADb>) = EventV2(
    id = this.slugId,
    name = this.name,
    address = this.address.convertToModel(),
    startDate = this.startDate,
    endDate = this.endDate,
    menus = menus.map { it.convertToModel() },
    qanda = qanda.map { it.convertToModel() },
    coc = coc,
    openfeedbackProjectId = this.openFeedbackId,
    features = this.convertToFeaturesActivatedModel(hasPartnerList, qanda.isNotEmpty()),
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun EventDb.convertToModelV3(hasPartnerList: Boolean, hasQandA: Boolean) = EventV3(
    id = this.slugId,
    name = this.name,
    address = this.address.convertToModel(),
    startDate = this.startDate,
    endDate = this.endDate,
    menus = menus.map { it.convertToModel() },
    coc = coc,
    openfeedbackProjectId = this.openFeedbackId,
    features = this.convertToFeaturesActivatedModel(hasPartnerList, hasQandA),
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun LunchMenuInput.convertToDb() = LunchMenuDb(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
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

fun EventInput.convertToDb(event: EventDb, addressDb: AddressDb) = EventDb(
    slugId = event.name.slug(),
    year = event.year,
    name = this.name,
    apiKey = event.apiKey,
    defaultLanguage = event.defaultLanguage,
    address = addressDb,
    openFeedbackId = openFeedbackId ?: event.openFeedbackId,
    conferenceHallConfig = this.conferenceHallConfigInput?.convertToDb(),
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    wldConfig = this.wldConfig?.convertToDb(),
    menus = event.menus,
    coc = event.coc,
    startDate = this.startDate,
    endDate = this.endDate,
    sponsoringTypes = this.sponsoringTypes,
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    published = published,
    updatedAt = this.updatedAt
)

fun CreatingEventInput.convertToDb(addressDb: AddressDb, language: String) = EventDb(
    slugId = name.slug(),
    year = year,
    apiKey = UUID.randomUUID().toString(),
    name = this.name,
    defaultLanguage = language,
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
