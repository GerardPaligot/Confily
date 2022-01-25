package com.paligot.conferences.android

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun Activity.launchUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}