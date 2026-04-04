package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionVoteStats(
    val id: String,
    val title: String,
    val language: String,
    val level: String?,
    val format: String?,
    val speakers: List<String>,
    @SerialName("vote_count")
    val voteCount: Long
)
