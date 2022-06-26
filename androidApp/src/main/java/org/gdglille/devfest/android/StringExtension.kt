package org.gdglille.devfest.android

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Activity.launchUrl(url: String) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}
