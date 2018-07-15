package otus.homework.fourth.service

import arrow.core.Try
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.logging.LogLevel.INFO
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import otus.homework.fourth.model.Question
import ru.tinkoff.eclair.annotation.Log
import java.util.*

@Service
@ConfigurationProperties(prefix = "spring.mvc")
class QuestionReaderService(
        private val messageSource: MessageSource
) {
    lateinit var locale:Locale
    @Log(INFO)
    fun readQuestions(): Try<List<Question>> = Try {
        (1..5).map {
            Question(
                    question = messageSource.getMessage("quizz.qsn_$it", locale),
                    answer = messageSource.getMessage("quizz.ans_$it", locale)
            )
        }
    }
}

@Throws(NoSuchMessageException::class)
fun MessageSource.getMessage(code:String, locale:Locale) = this.getMessage(code, arrayOf(), locale)