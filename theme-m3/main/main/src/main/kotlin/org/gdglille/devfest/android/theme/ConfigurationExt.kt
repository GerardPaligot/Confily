package org.gdglille.devfest.android.theme

import android.content.res.Configuration

val Configuration.isPortrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT
