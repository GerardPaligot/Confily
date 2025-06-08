package com.paligot.confily.backend.internals.infrastructure.firestore

data class LunchMenuEntity(
    val name: String = "",
    val dish: String = "",
    val accompaniment: String = "",
    val dessert: String = ""
)
