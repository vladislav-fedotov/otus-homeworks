package otus.homework.fourth.service

import arrow.core.Try
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import otus.homework.fourth.model.Question
import otus.homework.fourth.model.QuestionResult
import otus.homework.fourth.model.QuizzResult


class QuizzServiceTest {
    @Test
    fun `test main logic flow in startQuizz() method`() {
        // given
        val q1 = Question("Q1", "A1")
        val q2 = Question("Q2", "A2")
        val q3 = Question("Q3", "A3")
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn listOf(
                                    Try.just(
                                            listOf(q1, q2, q3)
                                    )
                            )
                        },
                        mock()
                )
        )
        doReturn("user answer").`when`(quizzService).readAnswer()

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService).printQuestion(q1)
        verify(quizzService).printQuestion(q2)
        verify(quizzService).printQuestion(q3)

        verify(quizzService).showResults(
                QuizzResult(
                        questionResults = listOf(
                                QuestionResult(
                                        question = q1.question,
                                        expectedAnswer = q1.answer,
                                        actualAnswer = "user answer"
                                ),
                                QuestionResult(
                                        question = q2.question,
                                        expectedAnswer = q2.answer,
                                        actualAnswer = "user answer"
                                ),
                                QuestionResult(
                                        question = q3.question,
                                        expectedAnswer = q3.answer,
                                        actualAnswer = "user answer"
                                )
                        )
                )
        )
    }
}
