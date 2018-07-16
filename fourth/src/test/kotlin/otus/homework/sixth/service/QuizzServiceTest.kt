package otus.homework.sixth.service

import arrow.core.Try
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.NoSuchMessageException
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import otus.homework.fourth.model.Question
import otus.homework.fourth.model.QuestionResult
import otus.homework.fourth.model.QuizzResult
import otus.homework.fourth.service.GreetingService
import otus.homework.fourth.service.QuestionReaderService
import otus.homework.fourth.service.QuizzService
import ru.tinkoff.eclair.logger.ManualLogger
import java.io.FileNotFoundException

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfiguration::class])
class QuizzServiceTest {

    @Autowired
    lateinit var questionReaderService: QuestionReaderService

    @SpyBean
    lateinit var quizzService: QuizzService

    @Test
    fun `test main logic flow in startQuizz() method`() {
        // given
        val q1 = Question("Q1", "A1")
        val q2 = Question("Q2", "A2")
        val q3 = Question("Q3", "A3")
        doReturn(Try.just(listOf(q1, q2, q3))).`when`(questionReaderService).readQuestions()
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

        doReturn(Try.just(listOf(q1))).`when`(questionReaderService).readQuestions()
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
        doReturn(Try { throw NoSuchMessageException("Message") }).`when`(questionReaderService).readQuestions()

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService, never()).printQuestion(any())

        verify(quizzService, never()).showResults(any())
    }

    @Test
    fun `test when readQuestions() throw FileNotFoundException`() {
        // given
        doReturn(Try { throw FileNotFoundException("Message") }).`when`(questionReaderService).readQuestions()

        // when
        quizzService.startQuizz()

        // then
        verify(quizzService, never()).printQuestion(any())

        verify(quizzService, never()).showResults(any())
    }
}

@Configuration
class TestConfiguration{
    @MockBean
    lateinit var manualLogger: ManualLogger

    @MockBean
    lateinit var greetingService: GreetingService

    @MockBean
    lateinit var questionReaderService: QuestionReaderService
}
