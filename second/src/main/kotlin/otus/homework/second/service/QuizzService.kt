package otus.homework.second.service

import org.springframework.boot.logging.LogLevel.INFO
import org.springframework.stereotype.Service
import otus.homework.second.model.Question
import otus.homework.second.model.QuestionResult
import otus.homework.second.model.QuizzResult
import otus.homework.second.model.toQuizzResult
import ru.tinkoff.eclair.annotation.Log
import ru.tinkoff.eclair.logger.ManualLogger
import java.io.FileNotFoundException

@Service
class QuizzService(
        val greetingsService: GreetingService,
        val questionsReaderService: QuestionReaderService,
        val log: ManualLogger
) {
    @Log(INFO)
    fun startQuizz() =
            greetingsService.greeting().also {
                questionsReaderService.readQuestions()
                        .fold({ throwable ->
                            when (throwable) {
                                is FileNotFoundException -> log.error("File with questions was not found!")
                                else -> log.error(throwable.toString())
                            }
                        })
                        { questions ->
                            questions
                                    .map { question ->
                                        printQuestion(question).let {
                                            QuestionResult(
                                                    question = question.question,
                                                    expectedAnswer = question.answer,
                                                    actualAnswer = readAnswer()
                                            )
                                        }
                                    }
                                    .toQuizzResult()
                                    .also { showResults(it) }
                        }
            }

    internal fun printQuestion(question: Question) {
        println(question.question)
        print(">")
    }

    internal fun readAnswer() = readLine() ?: ""

    internal fun showResults(quizzResult: QuizzResult) {
        println("------------------------------- Quizz Results --------------------------------")
        println("Asked ${quizzResult.numberOfQuestions} questions")
        println("Correctly answered: ${quizzResult.answeredCorrectly}")
        println("Incorrectly answered: ${quizzResult.answeredIncorrectly}")
        println()
    }
}