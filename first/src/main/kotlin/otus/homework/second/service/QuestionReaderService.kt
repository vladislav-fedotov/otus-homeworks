package otus.homework.second.service

import org.springframework.stereotype.Service
import otus.homework.second.model.Question
import java.io.File
import java.io.FileNotFoundException

@Service
class QuestionReaderService {
    fun readQuestions(fileName: String): List<Question> =
            try {
                File(fileName).bufferedReader().readLines().map {
                    val (question, answer) = it.split(",")
                    Question(question, answer)
                }
            } catch (e: FileNotFoundException) {
                println("File with questions not found")
                emptyList()
            }
}