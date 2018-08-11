package otus.homework.sixth

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import otus.homework.sixth.dao.AuthorDao
import otus.homework.sixth.dao.BookDao
import otus.homework.sixth.dao.GenreDao
import otus.homework.sixth.model.Author
import otus.homework.sixth.model.Book
import otus.homework.sixth.model.Genre
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@SpringBootTest
@ExtendWith(SpringExtension::class)
class DaoTest {

    @Autowired
    lateinit var bookDao: BookDao

    @Autowired
    lateinit var authorDao: AuthorDao

    @Autowired
    lateinit var genreDao: GenreDao

    @Test
    fun whenSaveBook_idShouldSet() {
        val book = bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        assertNotEquals(0, book.id)
    }

    @Test
    fun whenFindAllBooks_allBooksShouldReturned() {
        bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        val books = bookDao.findAll()

        assertTrue(books.isNotEmpty())
    }

    @Test
    fun whenFindBookById_andBookExists_bookShouldReturned() {
        val book = bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        val foundByIdBook = bookDao.findById(book.id)

        assertNotNull(foundByIdBook)
        assertEquals(foundByIdBook!!.id, book.id)
        assertEquals(foundByIdBook.title, book.title)
    }

    @Test
    fun whenSaveBook_authorDaoShouldFindAuthor() {
        val book = bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        val author = authorDao.findById(book.author.id)

        assertNotNull(author)
        assertEquals("Fi", author!!.firstName)
        assertEquals("Fa", author.familyName)
    }

    @Test
    fun whenSaveBook_genreDaoShouldFindAuthor() {
        val book = bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        val genre = genreDao.findById(book.genre.id)

        assertNotNull(genre)
        assertEquals("G", genre!!.code)
        assertEquals("g", genre.name)
    }

    @Test
    fun whenAuthorAlreadyExist_itShouldBeAddedWithBook() {
        val author = authorDao.save(
                Author(
                        firstName = "Hey",
                        familyName = "Ho"
                )
        )

        val authorsNumberBeforeSave = authorDao.count()

        bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = author
                )
        )

        val authorsNumberAfterSave = authorDao.count()

        assertEquals(authorsNumberBeforeSave, authorsNumberAfterSave)
    }

    @Test
    fun whenGenreAlreadyExist_itShouldBeAddedWithBook() {
        val genre = genreDao.save(
                Genre(
                        name = "let",
                        code = "LET"
                )
        )

        val genresNumberBeforeSave = genreDao.count()

        bookDao.save(
                Book(
                        genre = genre,
                        title = "T t",
                        isbn = "ab-cd-${Random().nextInt().toChar()}",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )

        val genresNumberAfterSave = genreDao.count()

        assertEquals(genresNumberBeforeSave, genresNumberAfterSave)
    }
}