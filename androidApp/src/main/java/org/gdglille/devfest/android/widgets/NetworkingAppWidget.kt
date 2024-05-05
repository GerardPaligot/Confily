package org.gdglille.devfest.android.widgets

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.widgets.feature.NetworkingWidget
import org.gdglille.devfest.android.widgets.style.Conferences4HallGlanceTheme
import org.gdglille.devfest.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NetworkingAppWidget : GlanceAppWidget(), KoinComponent {
    private val userRepository: UserRepository by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Conferences4HallGlanceTheme {
                NetworkingWidget(
                    userRepository = userRepository,
                    iconId = R.drawable.ic_launcher_foreground,
                    onNewProfile = actionStartActivity(
                        intent = Intent(
                            Intent.ACTION_VIEW,
                            "c4h://event/${Screen.NewProfile.route}".toUri()
                        )
                    ),
                    onMyProfile = actionStartActivity(
                        intent = Intent(
                            Intent.ACTION_VIEW,
                            "c4h://event/${Screen.MyProfile.route}".toUri()
                        )
                    )
                )
            }
        }
    }
}
