package otus.homework.twelfth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import otus.homework.twelfth.model.Author
import otus.homework.twelfth.model.Book
import otus.homework.twelfth.model.Genre
import otus.homework.twelfth.dao.BookRepository

@SpringBootApplication
class Twelfth

fun main(args: Array<String>) {
    val context = SpringApplication.run(Twelfth::class.java)
    val bookRepository = context.getBean(BookRepository::class.java)
    println("saving book...")
    bookRepository.save(
            Book(
                    genre = Genre(name = "Fantasy", code = "FAN"),
                    author = Author(firstName = "John R. R.", familyName = "Tolkien"),
                    title = "The Silmarillion",
                    isbn = "0-04-823139-8",
                    publicationYear = 1977,
                    numberOfPages = 365,
                    publisher = "George Allen & Unwin"
            )
    )
    println("book saved...")

    bookRepository.findAll().forEach(::println)

}

