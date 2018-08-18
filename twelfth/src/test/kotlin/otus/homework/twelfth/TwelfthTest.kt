package otus.homework.twelfth

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import otus.homework.twelfth.dao.BookRepository
import otus.homework.twelfth.model.Author
import otus.homework.twelfth.model.Book
import otus.homework.twelfth.model.Genre
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.AfterEach



@DataMongoTest
@ExtendWith(SpringExtension::class)
class TwelfthTest {

    @Autowired
    lateinit var bookRepository: BookRepository

    val book = Book(
            genre = Genre(name = "Fantasy", code = "FAN"),
            author = Author(firstName = "John R. R.", familyName = "Tolkien"),
            title = "The Silmarillion",
            isbn = "0-04-823139-8",
            publicationYear = 1977,
            numberOfPages = 365,
            publisher = "George Allen & Unwin"
    )

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
    }

    @Test
    fun `when saving book in db count must equal one`() {
        bookRepository.save(book)

        assertEquals(1, bookRepository.count())
    }

    @Test
    fun `when saving book in db book id must not be null`() {
        val book = bookRepository.save(book)

        assertNotNull(book.id)
    }

    @Test
    fun `when delete by id was executed count must return zero`() {
        val book = bookRepository.save(book)

        assertEquals(1, bookRepository.count())

        bookRepository.deleteById(book.id!!)

        assertEquals(0, bookRepository.count())
    }

    @Test
    fun `when new book isbn set it must be returned by book's id`() {
        val savedBookWithId = bookRepository.save(book)

        val updatedIsbn = "1-21-474826-3"
        val updatedBook = savedBookWithId.copy(isbn = updatedIsbn)

        bookRepository.save(updatedBook)

        val bookFoundById = bookRepository.findById(savedBookWithId.id!!).get()

        assertEquals(updatedIsbn, bookFoundById.isbn)
        assertEquals(1, bookRepository.count())
    }


}
