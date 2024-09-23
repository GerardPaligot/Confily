package com.paligot.confily.wear.presentation.partners

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.paligot.confily.wear.presentation.partners.presentation.PartnerVM
import com.paligot.confily.wear.presentation.partners.presentation.PartnersVM

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
