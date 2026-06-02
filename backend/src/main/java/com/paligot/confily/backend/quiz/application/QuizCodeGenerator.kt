package com.paligot.confily.backend.quiz.application

import kotlin.random.Random

object QuizCodeGenerator {
    // Uppercase letters excluding I and O to avoid confusion with 1 and 0.
    private const val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    private const val LENGTH = 4

    fun generate(random: Random = Random.Default): String =
        (1..LENGTH).map { ALPHABET[random.nextInt(ALPHABET.length)] }.joinToString("")
}
