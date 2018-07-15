package otus.homework.fourth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class GreetingService(
        private val messageSource: MessageSource,
        @Value("\${spring.mvc.locale}") private val locale: Locale
) {
    fun greeting() {
        print(messageSource.getMessage("greeting.title", locale))
        val userNaming = readUserFirstAndLastName()
        when (userNaming?.size) {
            1 -> println("${messageSource.getMessage("greeting.hi", locale)} ${userNaming[0]}!")
            2 -> println("${messageSource.getMessage("greeting.hi", locale)} ${userNaming[0]} ${userNaming[1]}!")
            else -> println(messageSource.getMessage("greeting.na", locale))
        }
    }

    internal fun readUserFirstAndLastName() =
            readLine()?.split(" ")?.filter { it.isNotBlank() }
}