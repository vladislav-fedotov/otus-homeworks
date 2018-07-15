package otus.homework.sixth.service

import arrow.core.Try
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.logging.LogLevel.INFO
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import otus.homework.fifth.model.Question
import otus.homework.fifth.util.measure
import ru.tinkoff.eclair.annotation.Log
import java.util.*

@Service
class QuestionReaderService(
        private val messageSource: MessageSource,
        @Value("\${spring.mvc.locale}") private val locale: Locale
) {
    @Log(INFO)
    fun readQuestions(): Try<List<Question>> = measure {
        Try {
            (1..5).map {
                Question(
                        question = messageSource.getMessage("quizz.qsn_$it", locale),
                        answer = messageSource.getMessage("quizz.ans_$it", locale)
                )
            }
        }
    }
}

@Throws(NoSuchMessageException::class)
fun MessageSource.getMessage(code: String, locale: Locale) = this.getMessage(code, arrayOf(), locale)