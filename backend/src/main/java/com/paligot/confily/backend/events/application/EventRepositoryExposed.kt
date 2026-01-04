package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.addresses.infrastructure.exposed.toEntity
import com.paligot.confily.backend.addresses.infrastructure.exposed.toModel
import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.domain.EventRepository
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventFeatureEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventSocialsTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.events.infrastructure.exposed.FeatureKey
import com.paligot.confily.backend.events.infrastructure.exposed.toEventItemList
import com.paligot.confily.backend.events.infrastructure.firestore.Sponsorship
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import com.paligot.confily.backend.integrations.infrastructure.exposed.IntegrationEntity
import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenuEntity
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenusTable
import com.paligot.confily.backend.menus.infrastructure.exposed.toModel
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.toModelV1
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.toModel
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.models.Address
import com.paligot.confily.models.CreatedEvent
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventList
import com.paligot.confily.models.EventPartners
import com.paligot.confily.models.EventV2
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.EventV4
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.CreatingEventInput
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

@Suppress("TooManyFunctions") // Repository classes naturally have multiple methods
class EventRepositoryExposed(
    private val database: Database,
    private val geocodeApi: GeocodeApi
) : EventRepository {

    override suspend fun list(): EventList = transaction(db = database) {
        val events = EventEntity.all()
            .orderBy(EventsTable.startDate to SortOrder.ASC)
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        EventList(
            future = events
                .filter { it.startDate >= now }
                .sortedBy { it.startDate }
                .map { it.toEventItemList() },
            past = events
                .filter { it.endDate < now }
                .sortedByDescending { it.endDate }
                .map { it.toEventItemList() }
        )
    }

    @Suppress("LongMethod")
    override suspend fun getWithPartners(eventId: String): Event = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val menus = LunchMenuEntity
            .findByEvent(eventUuid)
            .orderBy(LunchMenusTable.date to SortOrder.ASC)
            .map { it.toModel() }
        val qanda = QAndAEntity
            .findByLanguage(eventUuid, event.defaultLanguage)
            .map { qandaEntity ->
                val actions = QAndAActionEntity
                    .findByQAndAId(qandaEntity.id.value)
                    .orderBy(QAndAActionsTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                val acronyms = QAndAAcronymEntity
                    .findByQAndAId(qandaEntity.id.value)
                    .map { it.toModel() }
                qandaEntity.toModel(actions, acronyms)
            }
        val partners = EventPartners(
            golds = SponsoringTypeEntity
                .findByTypeName(eventUuid, Sponsorship.Gold.name)
                ?.let { sponsoringType ->
                    PartnerSponsorshipsTable
                        .partnerIds(sponsoringType.id.value)
                        .map { PartnerEntity[it].toModelV1() }
                        .sortedBy { it.name }
                } ?: emptyList(),
            silvers = SponsoringTypeEntity
                .findByTypeName(eventUuid, Sponsorship.Silver.name)
                ?.let { sponsoringType ->
                    PartnerSponsorshipsTable
                        .partnerIds(sponsoringType.id.value)
                        .map { PartnerEntity[it].toModelV1() }
                        .sortedBy { it.name }
                } ?: emptyList(),
            bronzes = SponsoringTypeEntity
                .findByTypeName(eventUuid, Sponsorship.Bronze.name)
                ?.let { sponsoringType ->
                    PartnerSponsorshipsTable
                        .partnerIds(sponsoringType.id.value)
                        .map { PartnerEntity[it].toModelV1() }
                        .sortedBy { it.name }
                } ?: emptyList(),
            others = SponsoringTypeEntity
                .findByTypeName(eventUuid, Sponsorship.Other.name)
                ?.let { sponsoringType ->
                    PartnerSponsorshipsTable
                        .partnerIds(sponsoringType.id.value)
                        .map { PartnerEntity[it].toModelV1() }
                        .sortedBy { it.name }
                } ?: emptyList()
        )
        val features1 = EventFeatureEntity
            .findByEvent(eventUuid)
            .associate { it.featureKey to it.enabled }
        val speakers = SpeakerEntity.findByEvent(eventUuid)
        val integration = IntegrationEntity.findIntegration(
            eventId = eventUuid,
            provider = IntegrationProvider.BILLETWEB,
            usage = IntegrationUsage.TICKETING
        )
        val features = FeaturesActivated(
            hasNetworking = features1[FeatureKey.Networking] ?: false,
            hasSpeakerList = speakers.empty().not(),
            hasPartnerList = partners.golds.isNotEmpty() ||
                partners.silvers.isNotEmpty() ||
                partners.bronzes.isNotEmpty() ||
                partners.others.isNotEmpty(),
            hasMenus = menus.isNotEmpty(),
            hasQAndA = qanda.isNotEmpty(),
            hasBilletWebTicket = integration != null
        )
        val socials = EventSocialsTable.findByEventId(eventUuid)
        Event(
            id = eventId,
            name = event.name,
            address = event.address?.toModel() ?: Address(
                formatted = emptyList(),
                address = "",
                city = "",
                country = "",
                countryCode = "",
                lat = 0.0,
                lng = 0.0
            ),
            startDate = event.startDate.toString(),
            endDate = event.endDate.toString(),
            partners = partners,
            menus = menus,
            qanda = qanda,
            coc = event.coc ?: "",
            features = features,
            twitterUrl = socials.find { it.type == SocialType.X }?.url,
            linkedinUrl = socials.find { it.type == SocialType.LinkedIn }?.url,
            faqLink = event.faqUrl,
            codeOfConductLink = event.cocUrl,
            updatedAt = event.updatedAt.toEpochMilliseconds()
        )
    }

    override suspend fun getV2(eventId: String): EventV2 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val menus = LunchMenuEntity
            .findByEvent(eventUuid)
            .orderBy(LunchMenusTable.date to SortOrder.ASC)
            .map { it.toModel() }
        val qanda = QAndAEntity
            .findByLanguage(eventUuid, event.defaultLanguage)
            .map { qandaEntity ->
                val actions = QAndAActionEntity
                    .findByQAndAId(qandaEntity.id.value)
                    .orderBy(QAndAActionsTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                val acronyms = QAndAAcronymEntity
                    .findByQAndAId(qandaEntity.id.value)
                    .map { it.toModel() }
                qandaEntity.toModel(actions, acronyms)
            }
        val partners = PartnerEntity.findByEvent(eventUuid)
        val features1 = EventFeatureEntity
            .findByEvent(eventUuid)
            .associate { it.featureKey to it.enabled }
        val speakers = SpeakerEntity.findByEvent(eventUuid)
        val integration = IntegrationEntity.findIntegration(
            eventId = eventUuid,
            provider = IntegrationProvider.BILLETWEB,
            usage = IntegrationUsage.TICKETING
        )
        val features = FeaturesActivated(
            hasNetworking = features1[FeatureKey.Networking] ?: false,
            hasSpeakerList = speakers.empty().not(),
            hasPartnerList = partners.empty().not(),
            hasMenus = menus.isNotEmpty(),
            hasQAndA = qanda.isNotEmpty(),
            hasBilletWebTicket = integration != null
        )
        val socials = EventSocialsTable.findByEventId(eventUuid)
        EventV2(
            id = eventId,
            name = event.name,
            address = event.address?.toModel() ?: Address(
                formatted = emptyList(),
                address = "",
                city = "",
                country = "",
                countryCode = "",
                lat = 0.0,
                lng = 0.0
            ),
            startDate = event.startDate.toString(),
            endDate = event.endDate.toString(),
            menus = menus,
            qanda = qanda,
            coc = event.coc ?: "",
            openfeedbackProjectId = null,
            features = features,
            contactPhone = event.contactPhone,
            contactEmail = event.contactEmail ?: "",
            twitterUrl = socials.find { it.type == SocialType.X }?.url,
            linkedinUrl = socials.find { it.type == SocialType.LinkedIn }?.url,
            faqLink = event.faqUrl,
            codeOfConductLink = event.cocUrl,
            updatedAt = event.updatedAt.toEpochMilliseconds()
        )
    }

    override suspend fun getV3(eventId: String): EventV3 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val menus = LunchMenuEntity
            .findByEvent(eventUuid)
            .orderBy(LunchMenusTable.date to SortOrder.ASC)
            .map { it.toModel() }
        val qanda = QAndAEntity.findByLanguage(eventUuid, event.defaultLanguage)
        val features1 = EventFeatureEntity
            .findByEvent(eventUuid)
            .associate { it.featureKey to it.enabled }
        val speakers = SpeakerEntity.findByEvent(eventUuid)
        val partners = PartnerEntity.findByEvent(eventUuid)
        val integration = IntegrationEntity.findIntegration(
            eventId = eventUuid,
            provider = IntegrationProvider.BILLETWEB,
            usage = IntegrationUsage.TICKETING
        )
        val features = FeaturesActivated(
            hasNetworking = features1[FeatureKey.Networking] ?: false,
            hasSpeakerList = speakers.empty().not(),
            hasPartnerList = partners.empty().not(),
            hasMenus = menus.isNotEmpty(),
            hasQAndA = qanda.empty().not(),
            hasBilletWebTicket = integration != null
        )
        val socials = EventSocialsTable.findByEventId(eventUuid)
        EventV3(
            id = eventId,
            name = event.name,
            address = event.address?.toModel() ?: Address(
                formatted = emptyList(),
                address = "",
                city = "",
                country = "",
                countryCode = "",
                lat = 0.0,
                lng = 0.0
            ),
            startDate = event.startDate.toString(),
            endDate = event.endDate.toString(),
            menus = menus,
            coc = event.coc ?: "",
            openfeedbackProjectId = null,
            features = features,
            contactPhone = event.contactPhone,
            contactEmail = event.contactEmail ?: "",
            twitterUrl = socials.find { it.type == SocialType.X }?.url,
            linkedinUrl = socials.find { it.type == SocialType.LinkedIn }?.url,
            faqLink = event.faqUrl,
            codeOfConductLink = event.cocUrl,
            updatedAt = event.updatedAt.toEpochMilliseconds()
        )
    }

    override suspend fun getV4(eventId: String): EventV4 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]

        // Fetch event features
        val features1 = EventFeatureEntity
            .findByEvent(eventUuid)
            .associate { it.featureKey to it.enabled }

        val speakers = SpeakerEntity.findByEvent(eventUuid)
        val partners = PartnerEntity.findByEvent(eventUuid)
        val menus = LunchMenuEntity
            .findByEvent(eventUuid)
            .orderBy(LunchMenusTable.date to SortOrder.ASC)
            .map { it.toModel() }
        val qanda = QAndAEntity.findByEvent(eventUuid)
        val integration = IntegrationEntity.findIntegration(
            eventId = eventUuid,
            provider = IntegrationProvider.BILLETWEB,
            usage = IntegrationUsage.TICKETING
        )
        val features = FeaturesActivated(
            hasNetworking = features1[FeatureKey.Networking] ?: false,
            hasSpeakerList = speakers.empty().not(),
            hasPartnerList = partners.empty().not(),
            hasMenus = menus.isNotEmpty(),
            hasQAndA = qanda.empty().not(),
            hasBilletWebTicket = integration != null
        )

        EventV4(
            id = event.id.value.toString(),
            name = event.name,
            address = event.address?.toModel() ?: Address(
                formatted = emptyList(),
                address = "",
                city = "",
                country = "",
                countryCode = "",
                lat = 0.0,
                lng = 0.0
            ),
            startDate = event.startDate.toString(),
            endDate = event.endDate.toString(),
            menus = menus,
            coc = event.coc ?: "",
            openfeedbackProjectId = null,
            features = features,
            contactPhone = event.contactPhone,
            contactEmail = event.contactEmail ?: "",
            socials = EventSocialsTable.findByEventId(eventUuid),
            faqLink = event.faqUrl,
            codeOfConductLink = event.cocUrl,
            updatedAt = event.updatedAt.toEpochMilliseconds()
        )
    }

    override suspend fun create(eventInput: CreatingEventInput, language: String): CreatedEvent {
        val geocode = geocodeApi.geocode(eventInput.address)
        return transaction(db = database) {
            val event = EventEntity.new {
                this.slug = eventInput.name.slug()
                this.name = eventInput.name
                this.startDate = LocalDate.parse(eventInput.startDate)
                this.endDate = LocalDate.parse(eventInput.endDate)
                this.defaultLanguage = language
                this.contactEmail = eventInput.contactEmail
                this.contactPhone = eventInput.contactPhone
                this.address = geocode.toEntity()
                this.cocUrl = null
                this.faqUrl = null
                this.publishedAt = null
            }
            CreatedEvent(
                eventId = event.id.value.toString(),
                apiKey = UUID.randomUUID().toString()
            )
        }
    }
}
