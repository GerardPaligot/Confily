package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.addresses.infrastructure.exposed.toModel
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventSocialsTable
import com.paligot.confily.backend.export.domain.ExportRepository
import com.paligot.confily.backend.map.infrastructure.exposed.MapEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramsTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapeEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapesTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapsTable
import com.paligot.confily.backend.map.infrastructure.exposed.toModel
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenuEntity
import com.paligot.confily.backend.menus.infrastructure.exposed.toModel
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.toModel
import com.paligot.confily.backend.team.infrastructure.exposed.TeamEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamTable
import com.paligot.confily.backend.team.infrastructure.exposed.toModel
import com.paligot.confily.models.CodeOfConduct
import com.paligot.confily.models.EventContact
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.QAndA
import com.paligot.confily.models.ThirdParty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExportEventRepositoryExposed(
    private val database: Database
) : ExportRepository<ExportEvent> {
    override suspend fun get(eventId: String): ExportEvent = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val socials = EventSocialsTable.findByEventId(eventUuid)
        val menus = LunchMenuEntity.findByEvent(eventUuid)
            .map { it.toModel() }
        val groups = TeamGroupEntity.findByEvent(eventUuid)
            .orderBy(TeamGroupsTable.displayOrder to SortOrder.ASC)
        val qanda = QAndAEntity.findByEvent(eventUuid)
            .groupBy { it.language }
            .map { (language, qandaEntities) ->
                language to qandaEntities.map { qandaEntity ->
                    val actions = QAndAActionEntity.findByQAndAId(qandaEntity.id.value)
                        .orderBy(QAndAActionsTable.displayOrder to SortOrder.ASC)
                        .map { it.toModel() }
                    val acronyms = QAndAAcronymEntity
                        .findByQAndAId(qandaEntity.id.value)
                        .map { it.toModel() }
                    qandaEntity.toModel(actions, acronyms)
                }
            }
            .associate { it }
        val maps = MapEntity
            .findByEvent(eventUuid)
            .orderBy(MapsTable.displayOrder to SortOrder.ASC)
            .map { mapEntity ->
                val shapes = MapShapeEntity
                    .findByMapId(mapEntity.id.value)
                    .orderBy(MapShapesTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                val pictograms = MapPictogramEntity
                    .findByMapId(mapEntity.id.value)
                    .orderBy(MapPictogramsTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                mapEntity.toModel(shapes, pictograms)
            }
        ExportEvent(
            id = event.id.value.toString(),
            name = event.name,
            address = event.address!!.toModel(),
            startDate = event.startDate.toString(),
            endDate = event.endDate.toString(),
            contact = EventContact(
                phone = event.contactPhone,
                email = event.contactEmail,
                socials = socials
            ),
            coc = CodeOfConduct(
                content = event.coc,
                link = event.cocUrl
            ),
            qanda = QAndA(content = qanda, link = event.faqUrl),
            menus = menus,
            features = FeaturesActivated(
                hasNetworking = false,
                hasSpeakerList = true,
                hasPartnerList = true,
                hasMenus = menus.isNotEmpty(),
                hasQAndA = true,
                hasBilletWebTicket = true
            ),
            team = groups.associate { group ->
                val members = TeamEntity
                    .findByGroupId(eventUuid, group.id.value)
                    .orderBy(TeamTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                group.name to members
            },
            maps = maps,
            thirdParty = ThirdParty(
                openfeedbackProjectId = null
            ),
            updatedAt = event.updatedAt.toEpochMilliseconds()
        )
    }
}
