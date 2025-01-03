package com.paligot.confily.widgets.panes

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.layout.fillMaxSize
import com.paligot.confily.networking.ui.models.UserProfileUi
import com.paligot.confily.widgets.ui.NoProfile
import com.paligot.confily.widgets.ui.R

@Composable
fun NetworkingScreen(
    userInfo: UserProfileUi?,
    @DrawableRes iconId: Int,
    onNewProfile: Action,
    onMyProfile: Action,
    modifier: GlanceModifier = GlanceModifier
) {
    Scaffold(
        titleBar = {
            val title = if (userInfo != null) {
                "${userInfo.firstName} ${userInfo.lastName}"
            } else {
                LocalContext.current.getString(R.string.widget_title_no_profile)
            }
            TitleBar(
                startIcon = ImageProvider(iconId),
                title = title
            )
        },
        content = {
            if (userInfo?.qrCode != null) {
                val qrCodeBitmap = remember(userInfo.qrCode) {
                    BitmapFactory.decodeByteArray(userInfo.qrCode, 0, userInfo.qrCode!!.size)
                }
                Image(
                    provider = ImageProvider(qrCodeBitmap),
                    contentDescription = LocalContext.current.getString(R.string.widget_semantic_qrcode),
                    modifier = GlanceModifier.fillMaxSize().clickable(onMyProfile)
                )
            } else {
                NoProfile(
                    modifier = GlanceModifier.clickable(onNewProfile)
                )
            }
        },
        modifier = modifier
    )
}
