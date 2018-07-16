package otus.homework.fourth.service

import arrow.core.Try
import arrow.core.getOrElse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import otus.homework.fourth.util.measure
import java.util.*

@Service
class GreetingService(
        private val messageSource: MessageSource,
        @Value("\${spring.mvc.locale}") private val locale: Locale
) {
    fun greeting() {
        measure {
            print(
                    Try { messageSource.getMessage("greeting.title", locale) }
                            .getOrElse { "\n--- Title ---\n>" }
            )
            val userFirstAndLastName = readUserFirstAndLastName()
            val salute = Try { messageSource.getMessage("greeting.hi", locale) }
                    .getOrElse { "Default greeting" }
            printSaluteWithUserName(userFirstAndLastName, salute)
        }
    }

    internal fun printSaluteWithUserName(userNaming: List<String>?, salute: String) {
        when (userNaming?.size) {
            1 -> println("$salute ${userNaming[0]}!")
            2 -> println("$salute ${userNaming[0]} ${userNaming[1]}!")
            else -> println("$salute!")
        }
    }

    internal fun readUserFirstAndLastName() =
            readLine()?.split(" ")?.filter { it.isNotBlank() }
}