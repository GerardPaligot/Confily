package com.paligot.conferences.android

import android.app.Application
import com.paligot.conferences.database.appContext

class Conferences4HallApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}