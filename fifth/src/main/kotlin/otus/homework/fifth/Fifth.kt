package otus.homework.fifth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import otus.homework.fifth.service.QuizzService


@SpringBootApplication
class Fourth {
    @Bean
    fun messageSource() =
            ReloadableResourceBundleMessageSource().apply {
                setBasename("/i18n/quizz")
                setDefaultEncoding("UTF-8")
            }
}

fun main(args: Array<String>) {
    val ctx = SpringApplication.run(Fourth::class.java)
    ctx.getBean(QuizzService::class.java).startQuizz()
}
