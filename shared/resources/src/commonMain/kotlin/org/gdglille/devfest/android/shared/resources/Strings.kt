package org.gdglille.devfest.android.shared.resources

data class Strings(
    val titles: TitleStrings,
    val texts: TextStrings
)

data class TitleStrings(
    val agendaBreak: String
)

data class TextStrings(
    val scheduleMinutes: (nbMinutes: Int) -> String,
    val speakersList: (count: Int, speakers: String) -> String,
    val levelAdvanced: String,
    val levelBeginner: String,
    val levelIntermediate: String,
    val speakerActivity: (jobTitle: String, company: String) -> String
)
