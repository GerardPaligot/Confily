package com.paligot.confily.infos.ui.models

import com.paligot.confily.socials.ui.models.SocialUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TeamMemberUi(
    val id: String,
    val displayName: String,
    val bio: String,
    val photoUrl: String?,
    val role: String,
    val socials: ImmutableList<SocialUi>
) {
    companion object {
        val fake = TeamMemberUi(
            id = "manu",
            displayName = "Emmanuel",
            bio = "Président",
            photoUrl = "",
            role = "Président",
            socials = persistentListOf(SocialUi.x)
        )
    }
}
