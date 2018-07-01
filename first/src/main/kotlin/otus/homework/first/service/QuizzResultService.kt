package otus.homework.first.service

import org.springframework.stereotype.Service
import otus.homework.first.model.QuizzResult

@Service
class QuizzResultService {
    fun showResults(quizzResult: QuizzResult) {
        quizzResult.showResults()
        println("----------------------------------- Thanks! -----------------------------------")
    }
}