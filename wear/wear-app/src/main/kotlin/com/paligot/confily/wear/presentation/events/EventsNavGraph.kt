package com.paligot.confily.wear.presentation.events

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.paligot.confily.wear.presentation.events.presentation.HorizontalEventPager
import com.paligot.confily.wear.presentation.events.presentation.ListEventVM

fun NavGraphBuilder.eventsGraph(
    navController: NavController
) {
    composable("events") {
        ListEventVM(
            onClick = {
                navController.navigate("event") {
                    this.popUpTo("events") {
                        inclusive = true
                    }
                }
            }
        )
    }
    composable("event") {
        HorizontalEventPager(
            onSchedulesClick = {
                navController.navigate("schedules")
            },
            onSpeakersClick = {
                navController.navigate("speakers")
            },
            onPartnersClick = {
                navController.navigate("partners")
            },
            onChangeEventClick = {
                navController.navigate("events") {
                    popUpTo("event") {
                        inclusive = true
                    }
                }
            }
        )
    }
}
