package otus.homework.second.service

import org.springframework.stereotype.Service
import otus.homework.second.model.Question
import otus.homework.second.model.QuestionResult

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