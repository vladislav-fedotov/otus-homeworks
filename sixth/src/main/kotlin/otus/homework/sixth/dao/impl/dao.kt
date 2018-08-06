package otus.homework.sixth.dao.impl

import arrow.core.Try
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
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
    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM BOOK", Long::class.java) ?: 0

    override fun save(book: Book): Book {
        val author = authorDao.findByEntity(book.author) ?: authorDao.save(book.author)
        val genre = genreDao.findByEntity(book.genre) ?: genreDao.save(book.genre)

        val keyHolder = GeneratedKeyHolder()
        jdbc.update(
                {
                    it.prepareStatement("INSERT INTO BOOK (GENRE_ID, TITLE, ISBN, PUBLICATION_YEAR, NUMBER_OF_PAGES, PUBLISHER) VALUES (?, ?, ?, ?, ?, ?)", arrayOf("ID")).apply {
                        setInt(1, genre.id)
                        setString(2, book.title)
                        setString(3, book.isbn)
                        setInt(4, book.publicationYear)
                        setInt(5, book.numberOfPages)
                        setString(6, book.publisher)
                    }
                },
                keyHolder
        )

        jdbc.update(
                "INSERT INTO BOOK_AUTHOR VALUES (?, ?)",
                keyHolder.key,
                author.id
        )
        return book.copy(
                id = keyHolder.key!!.toInt(),
                genre = genre,
                author = author
        )
    }

    override fun findById(id: Int): Book? =
            Try {
                jdbc.queryForObject("""
                SELECT
                    b.ID,
                    b.TITLE,
                    b.NUMBER_OF_PAGES,
                    b.PUBLICATION_YEAR,
                    b.ISBN,
                    b.PUBLISHER,
                    g.ID AS GENRE_ID,
                    g.NAME AS GENRE_NAME,
                    g.CODE AS GENRE_CODE,
                    a.ID AS AUTHOR_ID,
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
            }.fold({ null }, { it })

    override fun findAll(): List<Book> =
            jdbc.query("""
                SELECT
                    b.ID,
                    b.TITLE,
                    b.NUMBER_OF_PAGES,
                    b.PUBLICATION_YEAR,
                    b.ISBN,
                    b.PUBLISHER,
                    g.ID AS GENRE_ID,
                    g.NAME AS GENRE_NAME,
                    g.CODE AS GENRE_CODE,
                    a.ID AS AUTHOR_ID,
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
                genre = Genre(id = getInt("GENRE_ID"), name = getString("GENRE_NAME"), code = getString("GENRE_CODE")),
                title = getString("TITLE"),
                isbn = getString("ISBN"),
                publicationYear = getInt("PUBLICATION_YEAR"),
                numberOfPages = getInt("NUMBER_OF_PAGES"),
                publisher = getString("PUBLISHER"),
                author = Author(id = getInt("AUTHOR_ID"), firstName = getString("FIRST_NAME"), familyName = getString("FAMILY_NAME"))
        )
    }
}

@Repository
class AuthorDaoJdbc(
        private val jdbc: JdbcOperations
) : AuthorDao {
    override fun findByEntity(author: Author): Author? =
            Try {
                println("select author")
                jdbc.queryForObject(
                        "SELECT * FROM AUTHOR WHERE FIRST_NAME = ? AND FAMILY_NAME = ?",
                        arrayOf(author.firstName, author.familyName), AuthorMapper())
            }
                    .fold({ null }, { println("found author");it })

    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM AUTHOR", Long::class.java) ?: 0

    override fun save(author: Author): Author {
        val keyHolder = GeneratedKeyHolder()
        jdbc.update({
            it.prepareStatement("INSERT INTO AUTHOR (FIRST_NAME, FAMILY_NAME) VALUES (?, ?)", arrayOf("ID")).apply {
                setString(1, author.firstName)
                setString(2, author.familyName)
            }
        }, keyHolder)

        return author.copy(id = keyHolder.key!!.toInt())
    }

    override fun findById(id: Int): Author? =
            Try {
                jdbc.queryForObject("SELECT * FROM AUTHOR WHERE ID = ?", arrayOf(id), AuthorMapper())
            }.fold({ null }, { it })

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
    override fun findByEntity(genre: Genre): Genre? =
            Try {
                jdbc.queryForObject(
                        "SELECT * FROM GENRE WHERE NAME = ? AND CODE = ?",
                        arrayOf(genre.name, genre.code), GenreMapper())
            }.fold({ null }, { it })

    override fun count() =
            jdbc.queryForObject("SELECT COUNT(*) FROM GENRE", Long::class.java) ?: 0

    override fun save(genre: Genre): Genre {
        val keyHolder = GeneratedKeyHolder()
        jdbc.update({
            it.prepareStatement("INSERT INTO GENRE (NAME, CODE) VALUES (?, ?)", arrayOf("ID")).apply {
                setString(1, genre.name)
                setString(2, genre.code)
            }
        }, keyHolder)

        return genre.copy(id = keyHolder.key!!.toInt())
    }

    override fun findById(id: Int): Genre? =
            Try {
                jdbc.queryForObject("SELECT * FROM GENRE WHERE ID = ?", arrayOf(id), GenreMapper())
            }.fold({ null }, { it })

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
