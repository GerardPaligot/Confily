package com.paligot.confily.android.widgets

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import com.paligot.confily.android.R
import com.paligot.confily.core.repositories.UserRepository
import com.paligot.confily.navigation.Screen
import com.paligot.confily.widgets.presentation.NetworkingWidget
import com.paligot.confily.widgets.style.Conferences4HallGlanceTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NetworkingAppWidget : GlanceAppWidget(), KoinComponent {
    private val userRepository: UserRepository by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Conferences4HallGlanceTheme {
                NetworkingWidget(
                    userRepository = userRepository,
                    iconId = R.drawable.ic_campaign,
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
