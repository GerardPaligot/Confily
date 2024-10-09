package com.paligot.confily.wear.partners.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable

fun NavGraphBuilder.partnersGraph(
    navController: NavController
) {
    composable("partners") {
        PartnersVM(
            onClick = { navController.navigate("partners/$it") }
        )
    }
    composable("partners/{partnerId}") { backStackEntry ->
        PartnerVM(partnerId = backStackEntry.arguments?.getString("partnerId")!!)
    }
}
