package otus.homework.sixth.service

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.springframework.context.NoSuchMessageException
import otus.homework.fourth.service.GreetingService
import otus.homework.fourth.service.getMessage
import java.util.*


class GreetingServiceTest {
    @Test
    fun `test when readUserFirstAndLastName() reads first and second name`() {
        // given
        val locale = Locale("ru_RU")
        val title = "\nTitle\n>\n"
        val salute = "Hey ho"
        val greetingService = spy(
                GreetingService(
                        mock {
                            on { getMessage("greeting.title", locale) } doReturn title
                            on { getMessage("greeting.hi", locale) } doReturn salute
                        },
                        locale
                )
        )
        doReturn(listOf("John Snow")).`when`(greetingService).readUserFirstAndLastName()

        // when
        greetingService.greeting()

        // then
        verify(greetingService).printSaluteWithUserName(listOf("John Snow"), salute)
        verify(greetingService, times(1)).readUserFirstAndLastName()
    }

    @Test
    fun `test when readUserFirstAndLastName() reads only first name`() {
        // given
        val locale = Locale("ru_RU")
        val title = "\nTitle\n>\n"
        val salute = "Hey ho"
        val greetingService = spy(
                GreetingService(
                        mock {
                            on { getMessage("greeting.title", locale) } doReturn title
                            on { getMessage("greeting.hi", locale) } doReturn salute
                        },
                        locale
                )
        )
        doReturn(listOf("John")).`when`(greetingService).readUserFirstAndLastName()

        // when
        greetingService.greeting()

        // then
        verify(greetingService).printSaluteWithUserName(listOf("John"), salute)
        verify(greetingService, times(1)).readUserFirstAndLastName()
    }

    @Test
    fun `test that getMessage() throws NoSuchMessageException when getting salute`() {
        // given
        val locale = Locale("ru_RU")
        val title = "\nTitle\n>\n"
        val salute = "Bonjour"
        val greetingService = spy(
                GreetingService(
                        mock {
                            on { getMessage("greeting.title", locale) } doReturn title
                            on { getMessage("greeting.hi", locale) } doThrow NoSuchMessageException("Message")
                        },
                        locale
                )
        )
        doReturn(listOf("John")).`when`(greetingService).readUserFirstAndLastName()

        // when
        greetingService.greeting()

        // then
        verify(greetingService).printSaluteWithUserName(listOf("John"), salute)
        verify(greetingService, times(1)).readUserFirstAndLastName()
    }

    @Test
    fun `test that getMessage() throws NoSuchMessageException when getting title`() {
        // given
        val locale = Locale("ru_RU")
        val salute = "Hi"
        val greetingService = spy(
                GreetingService(
                        mock {
                            on { getMessage("greeting.title", locale) } doThrow NoSuchMessageException("Message")
                            on { getMessage("greeting.hi", locale) } doReturn salute
                        },
                        locale
                )
        )
        doReturn(listOf("John")).`when`(greetingService).readUserFirstAndLastName()

        // when
        greetingService.greeting()

        // then
        verify(greetingService).printSaluteWithUserName(listOf("John"), salute)
        verify(greetingService, times(1)).readUserFirstAndLastName()
    }
}
