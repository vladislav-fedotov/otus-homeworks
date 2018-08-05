package otus.homework.second.model

data class Question(val question: String, val answer: String)

data class QuestionResult(val question: String, val expectedAnswer: String, val actualAnswer: String) {
    val correct = expectedAnswer.equals(actualAnswer, true)
}

data class QuizzResult(private val questionResults: List<QuestionResult>) {
    val answeredCorrectly = questionResults.filter { it.correct }.size
    val answeredIncorrectly = questionResults.filterNot { it.correct }.size
    val numberOfQuestions = questionResults.size
}

fun List<QuestionResult>.toQuizzResult() = QuizzResult(this)