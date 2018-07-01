package otus.homework.first

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import otus.homework.first.service.QuizzService

@SpringBootApplication
class First {
    @Bean
    fun run(quizzService: QuizzService) = CommandLineRunner {
        quizzService.startQuizz()
    }
}

fun main(args: Array<String>) {
    runApplication<First>(*args)
}