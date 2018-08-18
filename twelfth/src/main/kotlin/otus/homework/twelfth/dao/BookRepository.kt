package otus.homework.twelfth.dao

import org.springframework.data.mongodb.repository.MongoRepository
import otus.homework.twelfth.model.Book

interface BookRepository : MongoRepository<Book, String>