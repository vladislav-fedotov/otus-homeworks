package otus.homework.second.service

import arrow.core.Try
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test as test
import otus.homework.second.model.Question
import otus.homework.second.model.QuestionResult
import otus.homework.second.model.QuizzResult


class QuizzServiceTest {
    @test
    fun `test main logic flow in startQuizz() method`() {
        // given
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn listOf(
                                    Try.just(
                                            listOf(
                                                    Question("Q1", "A1"),
                                                    Question("Q2", "A2"),
                                                    Question("Q3", "A3")
                                            )
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
        verify(quizzService).printQuestion(Question("Q1", "A1"))
        verify(quizzService).printQuestion(Question("Q2", "A2"))
        verify(quizzService).printQuestion(Question("Q3", "A3"))

        verify(quizzService).showResults(
                QuizzResult(
                        questionResults = listOf(
                                QuestionResult(
                                        question = "Q1",
                                        expectedAnswer = "A1",
                                        actualAnswer = "user answer"
                                ),
                                QuestionResult(
                                        question = "Q2",
                                        expectedAnswer = "A2",
                                        actualAnswer = "user answer"
                                ),
                                QuestionResult(
                                        question = "Q3",
                                        expectedAnswer = "A3",
                                        actualAnswer = "user answer"
                                )
                        )
                )
        )
    }
}
