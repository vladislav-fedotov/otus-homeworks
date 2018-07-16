package otus.homework.fifth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ReloadableResourceBundleMessageSource


@SpringBootApplication
class Fifth {
    @Bean
    fun messageSource() =
            ReloadableResourceBundleMessageSource().apply {
                setBasename("/i18n/quizz")
                setDefaultEncoding("UTF-8")
            }
}

fun main(args: Array<String>) {
    SpringApplication.run(Fifth::class.java)
}
