package com.paligot.confily.backend.internals.infrastructure.exposed

import com.paligot.confily.backend.ApplicationConfig
import com.paligot.confily.backend.activities.infrastructure.exposed.ActivitiesTable
import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventFeaturesTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventSocialsTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.BilletWebIntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.IntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.OpenPlannerIntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.SlackIntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.WebhookIntegrationsTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramsTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapesTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapsTable
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenusTable
import com.paligot.confily.backend.partners.infrastructure.exposed.JobsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSocialsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnersTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypesTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndATable
import com.paligot.confily.backend.schedules.infrastructure.exposed.SchedulesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTracksTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionCategoriesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionTagsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionsTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerSocialsTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import com.paligot.confily.backend.tags.infrastructure.exposed.TagsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamSocialsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    val allTables: List<Table> = listOf(
        EventsTable,
        EventFeaturesTable,
        EventSessionTracksTable,
        TeamGroupsTable,
        SponsoringTypesTable,
        CategoriesTable,
        FormatsTable,
        SpeakersTable,
        TagsTable,
        SessionsTable,
        SessionCategoriesTable,
        SessionSpeakersTable,
        SessionTagsTable,
        EventSessionsTable,
        PartnersTable,
        PartnerSponsorshipsTable,
        TeamTable,
        SocialsTable,
        EventSocialsTable,
        TeamSocialsTable,
        SpeakerSocialsTable,
        PartnerSocialsTable,
        JobsTable,
        ActivitiesTable,
        SchedulesTable,
        MapsTable,
        MapShapesTable,
        MapPictogramsTable,
        LunchMenusTable,
        QAndATable,
        QAndAActionsTable,
        QAndAAcronymsTable,
        AddressesTable,
        IntegrationsTable,
        BilletWebIntegrationsTable,
        OpenPlannerIntegrationsTable,
        SlackIntegrationsTable,
        WebhookIntegrationsTable
    )

    fun init(applicationConfig: ApplicationConfig): Database {
        val database = Database.connect(
            url = applicationConfig.databaseUrl,
            driver = applicationConfig.databaseDriver,
            user = applicationConfig.databaseUser,
            password = applicationConfig.databasePassword
        )

        // Create database schema (T006)
        transaction(database) {
            SchemaUtils.create(tables = allTables.toTypedArray())
        }

        return database
    }

    // For tests: create database with schema (T007)
    fun createTestDatabase(): Database {
        val database = Database.connect(
            url = "jdbc:h2:mem:test_${System.currentTimeMillis()};" +
                "MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )

        transaction(database) {
            SchemaUtils.create(tables = allTables.toTypedArray())
        }

        return database
    }
}
