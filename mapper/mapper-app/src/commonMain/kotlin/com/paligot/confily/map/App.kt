package com.paligot.confily.map

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.mapper.detail.di.mapDetailModule
import com.paligot.confily.mapper.detail.presentation.mapDetailGraph
import com.paligot.confily.mapper.list.di.mapListModule
import com.paligot.confily.mapper.list.presentation.mapListGraph
import com.paligot.confily.mapper.list.routes.MapList
import com.paligot.confily.style.theme.ConfilyTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    val navController = rememberNavController()
    ConfilyTheme {
        KoinApplication(application = {
            modules(buildConfigModule, mapListModule, mapDetailModule)
        }) {
            NavHost(
                navController = navController,
                startDestination = MapList,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                builder = {
                    mapListGraph(
                        eventId = "droidcon-london",
                        apiKey = "",
                        navController = navController
                    )
                    mapDetailGraph(navController = navController)
                }
            )
        }
    }
}
