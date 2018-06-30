package otus.homework.second.service

import org.springframework.stereotype.Service
import otus.homework.second.model.QuizzResult

@Service
class QuizzResultService {
    fun showResults(quizzResult: QuizzResult) {
        quizzResult.showResults()
        println("----------------------------------- Thanks! -----------------------------------")
    }
}