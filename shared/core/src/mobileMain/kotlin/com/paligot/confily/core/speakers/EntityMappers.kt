package com.paligot.confily.core.speakers

import com.paligot.confily.core.speakers.entities.SpeakerInfo
import com.paligot.confily.core.speakers.entities.SpeakerItem

internal val speakerInfoMapper = { id: String, display_name: String, pronouns: String?, bio: String,
    job_title: String?, company: String?, photo_url: String, _: String ->
    SpeakerInfo(
        id = id,
        displayName = display_name,
        bio = bio,
        photoUrl = photo_url,
        pronouns = pronouns,
        jobTitle = job_title,
        company = company
    )
}

internal val speakerItemMapper = { id: String, display_name: String, photo_url: String,
    job_title: String?, company: String? ->
    SpeakerItem(
        id = id,
        displayName = display_name,
        photoUrl = photo_url,
        jobTitle = job_title,
        company = company
    )
}
