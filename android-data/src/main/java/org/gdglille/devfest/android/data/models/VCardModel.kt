package org.gdglille.devfest.android.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.gdglille.devfest.models.UserNetworkingUi

@Parcelize
data class VCardModel(
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String
) : Parcelable

fun VCardModel.convertToModelUi(): UserNetworkingUi = UserNetworkingUi(
    email = email,
    firstName = firstName,
    lastName = lastName,
    company = company
)
