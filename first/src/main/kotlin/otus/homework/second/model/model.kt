package otus.homework.second.model

data class Question(val question: String, val expectedAnswer: String) {
    fun printQuestion() {
        println(this.question)
        print(">")
    }
}

data class QuestionResult(val question: Question, val actualAnswer: String) {
    val correct = question.expectedAnswer.equals(actualAnswer, true)
}

data class QuizzResult(val questionResults: List<QuestionResult>) {
    private val answeredCorrectly = questionResults.filter { it.correct }.size
    private val answeredIncorrectly = questionResults.filterNot { it.correct }.size

    fun showResults() {
        println("------------------------------- Quizz Results --------------------------------")
        println("Asked ${questionResults.size} questions")
        println("Correctly answered: $answeredCorrectly")
        println("Incorrectly answered: $answeredIncorrectly")
        println()
    }
}

fun List<QuestionResult>.toQuizzResult() = QuizzResult(this)