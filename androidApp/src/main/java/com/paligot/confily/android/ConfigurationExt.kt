package com.paligot.confily.android

import android.content.res.Configuration

val Configuration.isPortrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT
