package com.paligot.confily.resources

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "fr")
val FrStrings = Strings(
    titles = TitleStrings(
        agendaBreak = "Pause"
    ),
    texts = TextStrings(
        scheduleMinutes = { nbMinutes -> "$nbMinutes minutes" },
        speakersList = { count, speakers ->
            when (count) {
                0 -> speakers
                1 -> "$speakers et 1 autre"
                else -> "$speakers et ${count - 1} autres"
            }
        },
        levelAdvanced = "Avancé",
        levelBeginner = "Débutant",
        levelIntermediate = "Intermédiaire",
        speakerActivity = { jobTitle, company -> "$jobTitle à $company" }
    )
)
