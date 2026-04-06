package com.example.projeto_android_06_04.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto_android_06_04.data.room.db.DatabaseProvider
import com.example.projeto_android_06_04.data.room.entities.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application): AndroidViewModel(application) {
    private val dao = DatabaseProvider.getDatabase(application).libraryDao()

    private val _foundBook = MutableStateFlow<Book?>(null)
    val foundBook = _foundBook.asStateFlow()
    var searchIdInput by mutableStateOf("")
        private set

    val booksWithReview = dao.getBooksWithReviews().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addBook(title: String, author: String) {
        viewModelScope.launch {
            val bookId = dao.insertBook(Book(title = title, author = author))
        }
    }

    fun addReview(bookId: Int, rating: Float, comment: String) {
        viewModelScope.launch {
            dao.insertReview(
                com.example.projeto_android_06_04.data.room.entities.Review(
                    bookId = bookId,
                    rating = rating,
                    comment = comment
                )
            )
        }
    }

    fun addReviewToFirstBook(rating: Float, comment: String) {
        viewModelScope.launch {
            val books = booksWithReview.value
            if (books.isNotEmpty()) {
                val firstBookId = books.first().book.id
                addReview(firstBookId, rating, comment)
            }
        }
    }

    fun searchBook(id: Int) {
        viewModelScope.launch {
            dao.getBookById(id).collect { book ->
                _foundBook.value = book
            }
        }
    }

}