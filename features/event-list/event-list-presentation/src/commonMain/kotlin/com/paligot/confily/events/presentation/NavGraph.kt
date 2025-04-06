package com.paligot.confily.events.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.paligot.confily.events.routes.EventList
import com.paligot.confily.schedules.routes.ScheduleList

fun NavGraphBuilder.eventGraph(
    navController: NavController
) {
    composable<EventList> {
        EventListVM(
            onEventLoaded = {
                navController.navigate(ScheduleList) {
                    this.popUpTo(EventList) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
