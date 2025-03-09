package com.paligot.confily.map.editor.list.presentation

import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.paligot.confily.map.editor.detail.routes.MapDetail
import com.paligot.confily.map.editor.list.routes.MapList

fun NavGraphBuilder.mapListGraph(
    eventId: String,
    apiKey: String,
    navController: NavController
) {
    composable<MapList>(
        enterTransition = { fadeIn() }
    ) {
        MapListVM(
            eventId = eventId,
            apiKey = apiKey,
            onItemClick = { navController.navigate(MapDetail(eventId, apiKey, it)) }
        )
    }
}
