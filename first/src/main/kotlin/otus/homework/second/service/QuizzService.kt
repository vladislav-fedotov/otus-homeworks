package otus.homework.second.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import otus.homework.second.model.toQuizzResult

@Service
@ConfigurationProperties(prefix = "application")
class QuizzService(
        val greetingsService: GreetingService,
        val questionsReaderService: QuestionReaderService,
        val quizzResultService: QuizzResultService,
        val questionProcessorService: QuestionProcessorService
) {
    lateinit var quizzFilePath: String

    fun startQuizz() {
        greetingsService.greeting()
                .let {
                    questionsReaderService.readQuestions(quizzFilePath).map {
                        questionProcessorService.process(it)
                    }.toQuizzResult()
                }
                .also {
                    quizzResultService.showResults(it)
                }
    }

}