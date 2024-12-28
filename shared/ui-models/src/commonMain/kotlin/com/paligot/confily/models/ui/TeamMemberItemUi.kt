package com.paligot.confily.models.ui

data class TeamMemberItemUi(
    val id: String,
    val displayName: String,
    val role: String,
    val url: String?
) {
    companion object {
        val fake = TeamMemberItemUi(
            id = "manu",
            displayName = "Emmanuel",
            role = "Président",
            url = ""
        )
    }
}
