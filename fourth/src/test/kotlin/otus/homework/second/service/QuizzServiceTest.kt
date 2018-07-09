package otus.homework.second.service

import arrow.core.Try
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.springframework.context.NoSuchMessageException
import otus.homework.second.model.Question
import otus.homework.second.model.QuestionResult
import otus.homework.second.model.QuizzResult
import java.io.FileNotFoundException


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
                            on { readQuestions() } doReturn
                                    Try.just(
                                            listOf(q1, q2, q3)
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

    @Test
    fun `test when readAnswer() returns empty string`() {
        // given
        val q1 = Question("Q1", "A1")
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn
                                    Try.just(
                                            listOf(q1)
                                    )
                        },
                        mock()
                )
        )
        doReturn("").`when`(quizzService).readAnswer()

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService).printQuestion(q1)

        verify(quizzService).showResults(
                QuizzResult(
                        questionResults = listOf(
                                QuestionResult(
                                        question = q1.question,
                                        expectedAnswer = q1.answer,
                                        actualAnswer = ""
                                )
                        )
                )
        )
    }

    @Test
    fun `test when readQuestions() returns empty list`() {
        // given
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn
                                    Try.just(
                                            emptyList()
                                    )

                        },
                        mock()
                )
        )

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService, never()).printQuestion(any())

        verify(quizzService).showResults(
                QuizzResult(
                        questionResults = emptyList()
                )
        )
    }

    @Test
    fun `test when readQuestions() throw NoSuchMessageException`() {
        // given
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn
                                    Try { throw NoSuchMessageException("Message") }
                        },
                        mock {}
                )
        )

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService, never()).printQuestion(any())

        verify(quizzService, never()).showResults(any())
    }

    @Test
    fun `test when readQuestions() throw FileNotFoundException`() {
        // given
        val quizzService = spy(
                QuizzService(
                        mock {},
                        mock {
                            on { readQuestions() } doReturn
                                    Try { throw FileNotFoundException("Message") }
                        },
                        mock()
                )
        )

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService, never()).printQuestion(any())

        verify(quizzService, never()).showResults(any())
    }
}
