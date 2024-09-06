@file:Suppress("TooManyFunctions")

package com.paligot.confily.backend.events

import com.paligot.confily.backend.internals.slug
import com.paligot.confily.backend.qanda.QAndADb
import com.paligot.confily.backend.qanda.convertToModel
import com.paligot.confily.models.Address
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.EventLunchMenu
import com.paligot.confily.models.EventPartners
import com.paligot.confily.models.EventV2
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.inputs.BilletWebConfigInput
import com.paligot.confily.models.inputs.ConferenceHallConfigInput
import com.paligot.confily.models.inputs.CreatingEventInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.LunchMenuInput
import com.paligot.confily.models.inputs.OpenPlannerConfigInput
import com.paligot.confily.models.inputs.WldConfigInput
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

fun EventDb.convertToModel(partners: EventPartners, qanda: List<QAndADb>) = Event(
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

fun OpenPlannerConfigInput.convertToDb() = OpenPlannerConfigurationDb(
    eventId = eventId,
    privateId = privateId
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
    openPlannerConfig = this.openPlannerConfigInput?.convertToDb(),
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    wldConfig = this.wldConfig?.convertToDb(),
    eventSessionTracks = eventSessionTracks,
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

fun com.paligot.confily.models.inputs.CreatingEventInput.convertToDb(addressDb: AddressDb, language: String) = EventDb(
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
