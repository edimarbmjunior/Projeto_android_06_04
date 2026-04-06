package com.example.projeto_android_06_04.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.projeto_android_06_04.data.room.entities.Book
import com.example.projeto_android_06_04.data.room.entities.BookWithReview
import com.example.projeto_android_06_04.data.room.entities.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Insert
    suspend fun insertBook(book: Book): Long

    @Insert
    suspend fun insertReview(review: Review)

    @Query("SELECT * FROM Book")
    fun getAllBooks(): Flow<List<Book>>

    @Transaction
    @Query("SELECT * FROM Book")
    fun getBooksWithReviews(): Flow<List<BookWithReview>>

    @Query("SELECT * FROM Book WHERE id = :id")
    fun getBookById(id: Int): Flow<Book?>
}