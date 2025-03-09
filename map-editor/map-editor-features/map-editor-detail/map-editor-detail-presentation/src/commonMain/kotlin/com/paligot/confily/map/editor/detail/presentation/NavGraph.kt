package com.paligot.confily.map.editor.detail.presentation

import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.paligot.confily.map.editor.detail.routes.MapDetail

fun NavGraphBuilder.mapDetailGraph(navController: NavController) {
    composable<MapDetail>(
        enterTransition = { fadeIn() }
    ) {
        MapDetailVM(onBack = { navController.popBackStack() })
    }
}
