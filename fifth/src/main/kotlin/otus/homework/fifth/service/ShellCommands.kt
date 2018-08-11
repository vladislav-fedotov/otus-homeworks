package otus.homework.fifth.service

import arrow.core.Try
import arrow.core.getOrElse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.shell.Availability
import org.springframework.shell.Availability.available
import org.springframework.shell.Availability.unavailable
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import otus.homework.fifth.model.QuizzResult
import ru.tinkoff.eclair.logger.ManualLogger
import java.io.FileNotFoundException
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size


@ShellComponent
class ShellCommands(
        private val quizzService: QuizzService,
        private val messageSource: MessageSource,
        private val log: ManualLogger,
        @Value("\${spring.mvc.locale}") private val locale: Locale
) {

    private var started: Boolean = false
    private var finished: Boolean = false
    private var quizzResult: QuizzResult = QuizzResult(emptyList())

    @ShellMethod("Starts test")
    fun start() {
        started = true
        println(
                Try {
                    messageSource.getMessage("greeting.title", locale)
                }.getOrElse { "\n--- Title ---\n>" }
        )
    }

    @ShellMethod("Introduce yourself by typing first name and surname")
    fun intro(
            @ShellOption(value = ["-n", "--name"]) @NotEmpty @Size(min = 2, max = 20) name: String,
            @ShellOption(value = ["-s", "--surname"]) @NotEmpty @Size(min = 2, max = 20) surname: String
    ) {
        val salute = Try { messageSource.getMessage("greeting.hi", locale) }
                .getOrElse { "Default greeting" }
        println("$salute $name $surname!")
        quizzService.startQuizz().let {
            finished = true
            it.fold(
                    { throwable ->
                        when (throwable) {
                            is FileNotFoundException -> log.error("File with questions was not found!")
                            else -> log.error(throwable.toString())
                        }
                    })
            { this.quizzResult = it }
        }
    }

    @ShellMethod("Introduce yourself by typing first name and surname")
    fun result() {
        println("------------------------------- Quizz Results --------------------------------")
        println("Asked ${quizzResult.numberOfQuestions} questions")
        println("Correctly answered: ${quizzResult.answeredCorrectly}")
        println("Incorrectly answered: ${quizzResult.answeredIncorrectly}")
    }

    fun introAvailability(): Availability =
            if (started)
                available()
            else
                unavailable("test not started yet")

    fun startAvailability(): Availability =
            if (!started)
                available()
            else
                unavailable("test already started")

    fun resultAvailability(): Availability =
            if (finished)
                available()
            else
                unavailable("test already started")
}
