package otus.homework.sixth.dao

import otus.homework.sixth.model.Author
import otus.homework.sixth.model.Book
import otus.homework.sixth.model.Genre

interface BookDao {
    fun count(): Long
    fun save(book: Book): Book
    fun findById(id: Int): Book?
    fun findAll(): List<Book>
    fun findEntity(book: Book): Book?
}

interface AuthorDao {
    fun count(): Long
    fun save(author: Author): Author
    fun findById(id: Int): Author?
    fun findAll(): List<Author>
    fun findByFirstNameAndFamilyName(author: Author): Author?
}

interface GenreDao {
    fun count(): Long
    fun save(genre: Genre): Genre
    fun findById(id: Int): Genre?
    fun findAll(): List<Genre>
    fun findByNameAndCode(genre: Genre): Genre?
}