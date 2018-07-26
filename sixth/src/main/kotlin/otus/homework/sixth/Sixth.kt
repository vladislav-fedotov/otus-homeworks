package otus.homework.sixth

import org.h2.tools.Console
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import otus.homework.sixth.dao.AuthorDao
import otus.homework.sixth.dao.BookDao
import otus.homework.sixth.model.Author
import otus.homework.sixth.model.Book
import otus.homework.sixth.model.Genre


@SpringBootApplication
class Sixth

fun main(args: Array<String>) {
    val context = SpringApplication.run(Sixth::class.java)
    val bookDao = context.getBean(BookDao::class.java)
    val book = bookDao.findById(1)
    println(book)

    val books = bookDao.findAll()
    println(books)

    bookDao.save(
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

    println("Books:\n${bookDao.findAll()}\n")

    val authorDao = context.getBean(AuthorDao::class.java)
    authorDao.save(Author(firstName = "Lewis ", familyName = "Carroll"))
    println(authorDao.findAll())


//    Console.main(*args)
}