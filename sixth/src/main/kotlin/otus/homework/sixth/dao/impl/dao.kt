package otus.homework.sixth.dao.impl

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import otus.homework.sixth.dao.AuthorDao
import otus.homework.sixth.dao.BookDao
import otus.homework.sixth.dao.GenreDao
import otus.homework.sixth.model.Author
import otus.homework.sixth.model.Book
import otus.homework.sixth.model.Genre
import java.io.Serializable
import java.sql.ResultSet

@Repository
class BookDaoJdbc(
        private val jdbc: JdbcOperations,
        private val authorDao: AuthorDao,
        private val genreDao: GenreDao
) : BookDao {

    override fun findEntity(book: Book): Book? =
            jdbc.queryForObject("""
                SELECT
                    b.ID,
                    b.TITLE,
                    b.NUMBER_OF_PAGES,
                    b.PUBLICATION_YEAR,
                    b.ISBN,
                    b.PUBLISHER,
                    g.NAME AS GENRE_NAME,
                    g.CODE AS GENRE_CODE,
                    a.FIRST_NAME,
                    a.FAMILY_NAME
                FROM
                    BOOK b
                JOIN
                    GENRE g ON g.ID = b.GENRE_ID
                JOIN
                    BOOK_AUTHOR b_a ON b_a.BOOK_ID = b.ID
                JOIN
                    AUTHOR a ON a.ID = b_a.AUTHOR_ID
                WHERE
                    b.GENRE_ID = ?
                AND
                    b.TITLE = ?
                AND
                    b.NUMBER_OF_PAGES = ?
                AND
                    b.PUBLICATION_YEAR = ?
                AND
                    b.ISBN = ?
                AND
                    b.PUBLISHER = ?
                """,
                    with(book) {
                        arrayOf(
                                genre.id,
                                title,
                                numberOfPages,
                                publicationYear,
                                isbn,
                                publisher
                        )
                    },
                    BookMapper()
            )


    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM BOOK", Long::class.java) ?: 0

    override fun save(book: Book): Book {
        val author = authorDao.findByFirstNameAndFamilyName(book.author) ?: authorDao.save(book.author)
        val genre = genreDao.findByNameAndCode(book.genre) ?: genreDao.save(book.genre)

        jdbc.update(
                """INSERT INTO BOOK
                    (GENRE_ID, TITLE, ISBN, PUBLICATION_YEAR, NUMBER_OF_PAGES, PUBLISHER)
                    VALUES (?, ?, ?, ?, ?, ?)""",
                genre.id,
                book.title,
                book.isbn,
                book.publicationYear,
                book.numberOfPages,
                book.publisher
        )

        val bookWithId: Book = this.findEntity(book)!!

        jdbc.update(
                "INSERT INTO BOOK_AUTHOR VALUES (?, ?)",
                bookWithId.id,
                author.id
        )
        return bookWithId
    }

    override fun findById(id: Int): Book? =
            jdbc.queryForObject("""
                SELECT
                    b.ID,
                    b.TITLE,
                    b.NUMBER_OF_PAGES,
                    b.PUBLICATION_YEAR,
                    b.ISBN,
                    b.PUBLISHER,
                    g.NAME AS GENRE_NAME,
                    g.CODE AS GENRE_CODE,
                    a.FIRST_NAME,
                    a.FAMILY_NAME
                FROM
                    BOOK b
                JOIN
                    GENRE g ON g.ID = b.GENRE_ID
                JOIN
                    BOOK_AUTHOR b_a ON b_a.BOOK_ID = b.ID
                JOIN
                    AUTHOR a ON a.ID = b_a.AUTHOR_ID
                WHERE
                    b.ID = ?
                """, arrayOf(id), BookMapper())

    override fun findAll(): List<Book> =
            jdbc.query("""
                SELECT
                    b.ID,
                    b.TITLE,
                    b.NUMBER_OF_PAGES,
                    b.PUBLICATION_YEAR,
                    b.ISBN,
                    b.PUBLISHER,
                    g.NAME AS GENRE_NAME,
                    g.CODE AS GENRE_CODE,
                    a.FIRST_NAME,
                    a.FAMILY_NAME
                FROM
                    BOOK b
                INNER JOIN
                    GENRE g ON g.ID = b.GENRE_ID
                INNER JOIN
                    BOOK_AUTHOR b_a ON b_a.BOOK_ID = b.ID
                INNER JOIN
                    AUTHOR a ON a.ID = b_a.AUTHOR_ID
                """, BookMapper())
}

class BookMapper : RowMapper<Book>, Serializable {
    override fun mapRow(rs: ResultSet, rowNum: Int): Book? = with(rs) {
        Book(
                id = getInt("ID"),
                genre = Genre(name = getString("GENRE_NAME"), code = getString("GENRE_CODE")),
                title = getString("TITLE"),
                isbn = getString("ISBN"),
                publicationYear = getInt("PUBLICATION_YEAR"),
                numberOfPages = getInt("NUMBER_OF_PAGES"),
                publisher = getString("PUBLISHER"),
                author = Author(firstName = getString("FIRST_NAME"), familyName = getString("FAMILY_NAME"))
        )
    }
}

@Repository
class AuthorDaoJdbc(
        private val jdbc: JdbcOperations
) : AuthorDao {
    override fun findByFirstNameAndFamilyName(author: Author): Author? =
            jdbc.queryForObject("SELECT * FROM AUTHOR WHERE FIRST_NAME = ? AND FAMILY_NAME = ?", arrayOf(author.firstName, author.familyName), AuthorMapper())

    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM AUTHOR", Long::class.java) ?: 0

    override fun save(author: Author): Author =
            jdbc.update(
                    "INSERT INTO AUTHOR (FIRST_NAME, FAMILY_NAME) VALUES (?, ?)",
                    author.firstName,
                    author.familyName
            ).let { this.findByFirstNameAndFamilyName(author)!! }


    override fun findById(id: Int): Author? =
            jdbc.queryForObject("SELECT * FROM AUTHOR WHERE ID = ?", arrayOf(id), AuthorMapper())

    override fun findAll(): List<Author> =
            jdbc.query("SELECT * FROM AUTHOR", AuthorMapper())
}

class AuthorMapper : RowMapper<Author> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Author? =
            Author(
                    id = rs.getInt("ID"),
                    firstName = rs.getString("FIRST_NAME"),
                    familyName = rs.getString("FAMILY_NAME")
            )
}

@Repository
class GenreDaoJdbc(
        private val jdbc: JdbcOperations
) : GenreDao {
    override fun findByNameAndCode(genre: Genre): Genre? =
            jdbc.queryForObject("SELECT * FROM GENRE WHERE NAME = ? AND CODE = ?", arrayOf(genre.name, genre.code), GenreMapper())

    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM GENRE", Long::class.java) ?: 0

    override fun save(genre: Genre): Genre =
            jdbc.update(
                    "INSERT INTO GENRE (NAME, CODE) VALUES (?, ?)",
                    genre.name,
                    genre.code
            ).let { this.findByNameAndCode(genre)!! }


    override fun findById(id: Int): Genre? =
            jdbc.queryForObject("SELECT * FROM GENRE WHERE ID = ?", arrayOf(id), GenreMapper())

    override fun findAll(): List<Genre> =
            jdbc.query("SELECT * FROM GENRE", GenreMapper())
}

class GenreMapper : RowMapper<Genre> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Genre? =
            Genre(
                    id = rs.getInt("ID"),
                    name = rs.getString("NAME"),
                    code = rs.getString("CODE")
            )
}
