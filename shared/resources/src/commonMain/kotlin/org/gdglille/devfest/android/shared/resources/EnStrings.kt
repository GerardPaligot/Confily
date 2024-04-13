package org.gdglille.devfest.android.shared.resources

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "en", default = true)
val EnStrings = Strings(
    titles = TitleStrings(
        agendaBreak = "Break"
    ),
    texts = TextStrings(
        scheduleMinutes = { nbMinutes -> "$nbMinutes minutes" },
        speakersList = { count, speakers ->
            when (count) {
                0 -> speakers
                1 -> "$speakers and 1 other"
                else -> "$speakers and ${count - 1} others"
            }
        },
        levelAdvanced = "Advanced",
        levelBeginner = "Beginner",
        levelIntermediate = "Intermediate",
        speakerActivity = { jobTitle, company -> "$jobTitle at $company" }
    )
)
