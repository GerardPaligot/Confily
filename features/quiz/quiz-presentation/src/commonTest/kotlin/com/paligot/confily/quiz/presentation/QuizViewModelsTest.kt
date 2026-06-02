package com.paligot.confily.quiz.presentation

import com.paligot.confily.core.api.QuizException
import com.paligot.confily.core.quiz.QuizRepository
import com.paligot.confily.models.QuizAnswerOption
import com.paligot.confily.models.QuizAnsweredQuestion
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizAnswerInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private class FakeQuizRepository(
    private val questions: List<QuizQuestion> = emptyList(),
    private val questionsError: Throwable? = null,
    private val submitError: Throwable? = null,
    private val submitResult: QuizSubmissionResult? = null,
    private val saved: QuizSubmissionResult? = null,
    private val cumulative: Int? = null,
    private val username: String? = null
) : QuizRepository {
    var registered: String? = null
    override suspend fun register(username: String) { registered = username }
    override fun storedUsername(): Flow<String?> = flowOf(username)
    override suspend fun questions(code: String): List<QuizQuestion> =
        questionsError?.let { throw it } ?: questions
    override suspend fun submit(code: String, answers: List<QuizAnswerInput>): QuizSubmissionResult =
        submitError?.let { throw it } ?: submitResult!!
    override fun savedResult(code: String): QuizSubmissionResult? = saved
    override suspend fun cumulativeScore(): Int? = cumulative
}

private val sampleQuestion = QuizQuestion(
    id = "q1",
    order = 0,
    question = "Capital?",
    options = listOf(QuizAnswerOption("a1", "Paris", 0), QuizAnswerOption("a2", "Lyon", 1))
)

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelsTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun questions_load_maps_to_success() = runTest {
        val vm = QuizQuestionsViewModel(FakeQuizRepository(questions = listOf(sampleQuestion)))
        vm.load("ABCD")
        advanceUntilIdle()
        val state = vm.uiState.value
        assertIs<QuizQuestionsUiState.Success>(state)
        assertEquals(1, state.questions.size)
        assertEquals("Capital?", state.questions[0].question)
    }

    @Test
    fun questions_load_code_not_found_is_failure() = runTest {
        val vm = QuizQuestionsViewModel(FakeQuizRepository(questionsError = QuizException.CodeNotFound("ZZZZ")))
        vm.load("ZZZZ")
        advanceUntilIdle()
        assertIs<QuizQuestionsUiState.Failure>(vm.uiState.value)
    }

    @Test
    fun select_and_submit_returns_result() = runTest {
        val result = QuizSubmissionResult(
            correctCount = 1,
            totalCount = 1,
            perQuestion = listOf(QuizAnsweredQuestion("q1", "a1", "a1", true))
        )
        val vm = QuizQuestionsViewModel(
            FakeQuizRepository(questions = listOf(sampleQuestion), submitResult = result)
        )
        vm.load("ABCD")
        advanceUntilIdle()
        vm.select("q1", "a1")
        vm.submit("ABCD")
        advanceUntilIdle()
        assertIs<QuizQuestionsUiState.Submitted>(vm.uiState.value)
    }

    @Test
    fun submit_already_submitted_sets_already_state() = runTest {
        val vm = QuizQuestionsViewModel(
            FakeQuizRepository(questions = listOf(sampleQuestion), submitError = QuizException.AlreadySubmitted())
        )
        vm.load("ABCD")
        advanceUntilIdle()
        vm.select("q1", "a1")
        vm.submit("ABCD")
        advanceUntilIdle()
        assertIs<QuizQuestionsUiState.AlreadySubmitted>(vm.uiState.value)
    }

    @Test
    fun result_load_reads_saved_result() = runTest {
        val saved = QuizSubmissionResult(2, 3, perQuestion = emptyList())
        val vm = QuizResultViewModel(FakeQuizRepository(saved = saved))
        vm.load("ABCD")
        advanceUntilIdle()
        val state = vm.uiState.value
        assertIs<QuizResultUiState.Success>(state)
        assertEquals(2, state.result.correctCount)
    }

    @Test
    fun result_load_without_saved_is_already_completed() = runTest {
        val vm = QuizResultViewModel(FakeQuizRepository(saved = null))
        vm.load("ABCD")
        advanceUntilIdle()
        assertIs<QuizResultUiState.AlreadyCompleted>(vm.uiState.value)
    }

    @Test
    fun result_load_populates_question_labels() = runTest {
        val saved = QuizSubmissionResult(
            correctCount = 1,
            totalCount = 1,
            perQuestion = listOf(
                QuizAnsweredQuestion(
                    questionId = "q1",
                    chosenAnswerId = "a1",
                    correctAnswerId = "a1",
                    isCorrect = true
                )
            )
        )
        val vm = QuizResultViewModel(FakeQuizRepository(questions = listOf(sampleQuestion), saved = saved))
        vm.load("ABCD")
        advanceUntilIdle()
        val state = vm.uiState.value
        assertIs<QuizResultUiState.Success>(state)
        assertEquals("Capital?", state.result.perQuestion[0].question)
    }

    @Test
    fun home_register_delegates_to_repository() = runTest {
        val repo = FakeQuizRepository()
        val vm = QuizHomeViewModel(repo)
        vm.register("Alice")
        advanceUntilIdle()
        assertEquals("Alice", repo.registered)
    }

    @Test
    fun home_score_loads_cumulative() = runTest {
        val vm = QuizHomeViewModel(FakeQuizRepository(cumulative = 7))
        advanceUntilIdle()
        assertEquals(7, vm.score.value)
    }
}
