package otus.homework.fifth.service

import arrow.core.Try
import org.springframework.boot.logging.LogLevel.INFO
import org.springframework.stereotype.Service
import otus.homework.fifth.model.Question
import otus.homework.fifth.model.QuestionResult
import otus.homework.fifth.model.QuizzResult
import otus.homework.fifth.model.toQuizzResult
import ru.tinkoff.eclair.annotation.Log


@Service
class QuizzService(
        val questionsReaderService: QuestionReaderService
) {
    @Log(INFO)
    fun startQuizz(): Try<QuizzResult> =
            questionsReaderService.readQuestions()
                    .map { questions ->
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
                                .also { println("For test results, type use 'result' command") }
                                .toQuizzResult()
                    }

    internal fun printQuestion(question: Question) {
        println(question.question)
        print(">")
    }

    internal fun readAnswer() = readLine() ?: ""
}
