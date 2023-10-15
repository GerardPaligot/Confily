package org.gdglille.devfest

import android.content.Context
import android.content.Intent

interface AlarmIntentFactory {
    fun create(context: Context, id: String, title: String, text: String): Intent
}
