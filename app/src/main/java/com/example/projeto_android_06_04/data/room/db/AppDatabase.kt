package com.example.projeto_android_06_04.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projeto_android_06_04.data.room.daos.LibraryDao
import com.example.projeto_android_06_04.data.room.entities.Book
import com.example.projeto_android_06_04.data.room.entities.Review

@Database(
    entities = [Book::class, Review::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
}