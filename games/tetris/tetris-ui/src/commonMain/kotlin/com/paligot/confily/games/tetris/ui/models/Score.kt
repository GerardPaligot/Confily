package com.paligot.confily.games.tetris.ui.models

private const val ScoreEverySpirit = 12

data class Score(
    val value: Int = 0
) {
    fun compute(spirit: Spirit, lines: Int): Score {
        val scoreByLines = when (lines) {
            1 -> 100
            2 -> 300
            3 -> 700
            4 -> 1500
            else -> 0
        }
        val scoreBySpirit = if (spirit != Spirit.Empty) ScoreEverySpirit else 0
        return this.copy(value = value + scoreByLines + scoreBySpirit)
    }
}
