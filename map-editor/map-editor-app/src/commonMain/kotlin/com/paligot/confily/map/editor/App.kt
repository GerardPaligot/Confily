package com.paligot.confily.map.editor

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.BuildKonfig
import com.paligot.confily.map.editor.detail.di.mapDetailModule
import com.paligot.confily.map.editor.detail.presentation.mapDetailGraph
import com.paligot.confily.map.editor.list.di.mapListModule
import com.paligot.confily.map.editor.list.presentation.mapListGraph
import com.paligot.confily.map.editor.list.routes.MapList
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
                        eventId = BuildKonfig.DEFAULT_EVENT,
                        apiKey = BuildKonfig.API_KEY,
                        navController = navController
                    )
                    mapDetailGraph(navController = navController)
                }
            )
        }
    }
}
