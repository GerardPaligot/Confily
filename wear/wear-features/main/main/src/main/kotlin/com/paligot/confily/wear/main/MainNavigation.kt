package com.paligot.confily.wear.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.paligot.confily.wear.events.presentation.eventsGraph
import com.paligot.confily.wear.partners.presentation.partnersGraph
import com.paligot.confily.wear.schedules.presentation.scheduleGraph
import com.paligot.confily.wear.speakers.presentation.speakersGraph

@Composable
fun MainNavigation(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        eventsGraph(navController)
        scheduleGraph(navController)
        speakersGraph(navController)
        partnersGraph(navController)
    }
}
