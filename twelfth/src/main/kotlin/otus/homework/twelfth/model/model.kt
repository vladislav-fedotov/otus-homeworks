package otus.homework.twelfth.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class Author(
        val firstName: String,
        val familyName: String
)

data class Genre(
        val name: String,
        val code: String
)

@Document(collection = "books")
data class Book(
        @Id  val id: String? = null,
        val genre: Genre,
        val title: String,
        val isbn: String,
        val publicationYear: Int,
        val numberOfPages: Int,
        val publisher: String,
        val author: Author
)
