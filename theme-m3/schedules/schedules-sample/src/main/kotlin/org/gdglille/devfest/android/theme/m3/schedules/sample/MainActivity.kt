package org.gdglille.devfest.android.theme.m3.schedules.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.gdglille.devfest.android.core.sample.ScheduleWorkManager
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.schedules.feature.scheduleGraph
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

class MainActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val workManager = WorkManager.getInstance(this)
        setContent {
            val navController = rememberNavController()
            val config = LocalConfiguration.current
            val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
            val adaptiveInfo = WindowSizeClass.calculateFromSize(windowSize)
            Conferences4HallTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.ScheduleList.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    builder = {
                        scheduleGraph(
                            rootUri = "schedule",
                            isPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT,
                            adaptiveInfo = adaptiveInfo,
                            navController = navController,
                            enterTransition = EnterTransition.None,
                            popEnterTransition = EnterTransition.None,
                            exitTransition = ExitTransition.None,
                            popExitTransition = ExitTransition.None,
                            onShareClicked = {
                            },
                            onItineraryClicked = { _, _ ->
                            },
                            onScheduleStarted = {
                                val constraints = Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                                val request = OneTimeWorkRequestBuilder<ScheduleWorkManager>()
                                    .setConstraints(constraints)
                                    .build()
                                workManager.enqueue(request)
                            }
                        )
                    }
                )
            }
        }
    }
}
