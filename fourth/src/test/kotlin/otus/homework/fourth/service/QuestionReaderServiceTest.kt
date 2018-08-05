package otus.homework.fourth.service

import arrow.core.getOrDefault
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuestionReaderServiceTest {

    @Test
    fun `test when readQuestions() called 5 questions and 5 answers were read`() {
        // given
        val messageSourceMock = mock<MessageSource> {
            on { getMessage(any(), any(), any()) } doReturn "something"
        }
        val questionReaderService = spy(
                QuestionReaderService(messageSourceMock, Locale("ru_RU"))
        )

        // when
        val questions = questionReaderService.readQuestions()

        // then
        verify(messageSourceMock, times(10)).getMessage(any(), any(), any())
        assertTrue { questions.isSuccess() }
        assertEquals(5, questions.getOrDefault { emptyList() }.size)
    }

    @Test
    fun `test when readQuestions() returns failed Try`() {
        // given
        val messageSourceMock = mock<MessageSource> {
            on { getMessage(any(), any(), any()) } doThrow NoSuchMessageException("Key")
        }
        val questionReaderService = spy(
                QuestionReaderService(messageSourceMock, Locale("ru_RU"))
        )

        // when
        val questions = questionReaderService.readQuestions()

        // then
        verify(messageSourceMock, times(1)).getMessage(any(), any(), any())
        assertTrue { questions.isFailure() }
    }
}
