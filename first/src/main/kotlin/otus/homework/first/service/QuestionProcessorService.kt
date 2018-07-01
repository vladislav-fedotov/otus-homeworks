package otus.homework.first.service

import org.springframework.stereotype.Service
import otus.homework.first.model.Question
import otus.homework.first.model.QuestionResult

@Service
class QuestionProcessorService {
    fun process(question: Question):QuestionResult =
        question.printQuestion().let {
            QuestionResult(
                    question = question,
                    actualAnswer = readLine() ?: ""
            )
        }
}