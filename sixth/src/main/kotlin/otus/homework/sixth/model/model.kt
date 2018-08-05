package otus.homework.sixth.model

import java.io.Serializable

data class Author(
        val id: Int? = null,
        val firstName: String,
        val familyName: String
) : Serializable

data class Genre(
        val id: Int? = null,
        val name: String,
        val code: String
) : Serializable

data class Book(
        var id: Int? = 0,
        val genre: Genre,
        val title: String,
        val isbn: String,
        val publicationYear: Int,
        val numberOfPages: Int,
        val publisher: String,
        val author: Author
) : Serializable
