package com.paligot.confily.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VCardModel(
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String
) : Parcelable
