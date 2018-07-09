package otus.homework.second

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import otus.homework.second.service.QuizzService


@SpringBootApplication
class Fourth {
    @Bean
    fun run(quizzService: QuizzService) = CommandLineRunner {
        quizzService.startQuizz()
    }

    @Bean
    fun messageSource() =
            ReloadableResourceBundleMessageSource().apply {
                setBasename("/i18n/quizz")
                setDefaultEncoding("UTF-8")
            }
}

fun main(args: Array<String>) {
    runApplication<Fourth>(*args)
}
