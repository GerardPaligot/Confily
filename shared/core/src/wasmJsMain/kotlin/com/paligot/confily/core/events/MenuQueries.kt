package com.paligot.confily.core.events

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.events.MenuQueries.Scopes.MENUS
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow

class MenuQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val MENUS = "menus"
    }

    fun insertMenu(menuDb: MenuDb) {
        settings.putSerializableScoped(MENUS, menuDb.name, menuDb)
    }

    fun selectMenus(eventId: String): Flow<List<MenuDb>> =
        settings.combineAllSerializableScopedFlow<MenuDb>(MENUS) { it.eventId == eventId }
}
