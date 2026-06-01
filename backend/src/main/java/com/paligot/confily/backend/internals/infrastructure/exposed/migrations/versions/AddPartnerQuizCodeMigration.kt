package com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions

import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.Migration
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnersTable
import org.jetbrains.exposed.sql.SchemaUtils

object AddPartnerQuizCodeMigration : Migration {
    override val id = "20260601_add_partner_quiz_code"
    override val description = "Add quiz_code column to partners table for the partner quiz feature"

    override fun up() {
        SchemaUtils.createMissingTablesAndColumns(PartnersTable)
    }
}
