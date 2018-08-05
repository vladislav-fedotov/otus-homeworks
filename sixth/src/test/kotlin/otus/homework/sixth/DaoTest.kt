package otus.homework.sixth

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import otus.homework.sixth.dao.AuthorDao
import otus.homework.sixth.dao.BookDao
import otus.homework.sixth.dao.GenreDao
import otus.homework.sixth.model.Author
import otus.homework.sixth.model.Book
import otus.homework.sixth.model.Genre
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import javax.sql.DataSource


@ExtendWith(SpringExtension::class)
@DataJpaTest
//@SpringBootTest
//@ActiveProfiles("test")
@ContextConfiguration(classes = [(TestConfiguration::class)])
class DaoTest {

    @Autowired
    lateinit var bookDao: BookDao

    @Autowired
    lateinit var authorDao: AuthorDao

    @Autowired
    lateinit var genreDao: GenreDao

    @Test
    fun test() {
        bookDao.save(
                Book(
                        genre = Genre(
                                name = "g",
                                code = "G"
                        ),
                        title = "T t",
                        isbn = "ab-cd",
                        publicationYear = 2018,
                        numberOfPages = 5,
                        publisher = "Pub",
                        author = Author(
                                firstName = "Fi",
                                familyName = "Fa"
                        )
                )
        )
    }
}

@Configuration
class TestConfig {
    @Bean
    fun getJdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}