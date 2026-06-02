package com.paligot.confily.backend.quiz

import com.paligot.confily.backend.quiz.application.QuizCodeGenerator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuizCodeGeneratorTest {
    @Test
    fun `generates a 4-letter code`() {
        val code = QuizCodeGenerator.generate(Random(42))
        assertEquals(4, code.length)
    }

    @Test
    fun `uses only unambiguous uppercase letters`() {
        val allowed = "ABCDEFGHJKLMNPQRSTUVWXYZ".toSet()
        repeat(200) { seed ->
            val code = QuizCodeGenerator.generate(Random(seed.toLong()))
            assertTrue(code.all { it in allowed }, "Unexpected char in $code")
        }
    }
}
