package otus.homework.first.service

import org.springframework.stereotype.Service

@Service
class GreetingService {
    fun greeting() {
        print("\n------ Please enter your first name and first name separated by space ------\n>")
        val (firstName, lastName) = readUserFirstAndLastName()
        println("Hi $firstName $lastName!")
    }

    fun readUserFirstAndLastName(): List<String> = try {
        val split = readLine()?.split(' ')
        if (split == null || split.size < 2) listOf("NoFirstName", "NoLastName")
        else split
    } catch (e: Exception) {
        listOf("NoFirstName", "NoLastName")
    }
}