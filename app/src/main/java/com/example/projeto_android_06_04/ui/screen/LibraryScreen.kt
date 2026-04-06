package com.example.projeto_android_06_04.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto_android_06_04.viewmodels.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = viewModel()
) {
    val booksWithReviews = viewModel.booksWithReview.collectAsStateWithLifecycle()

    var title  by remember { mutableStateOf("") }
    var author  by remember { mutableStateOf("") }

    var reviewComent by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(0f) }

    val foundBook by viewModel.foundBook.collectAsStateWithLifecycle()
    var idBook by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {

        item{
            Text("Cadastrar Livro", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if(title.isNotBlank() && author.isNotBlank()) {
                        viewModel.addBook(title, author)
                        title = ""
                        author = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Livro")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Adicionar Avaliação ao Primeiro Livro", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reviewComent,
                onValueChange = { reviewComent = it },
                label = { Text("Comentário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reviewRating.toString(),
                onValueChange = { reviewRating = it.toFloatOrNull() ?: 0f },
                label = { Text("Avaliação (0-5)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (reviewComent.isNotBlank() && reviewRating in 0f..5f) {
                        viewModel.addReviewToFirstBook(reviewRating, reviewComent)
                        reviewComent = ""
                        reviewRating = 0f
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Avaliação")
            }
        }
        items(booksWithReviews.value) { bookWithReview ->
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Título: ${bookWithReview.book.title}", style = MaterialTheme.typography.titleMedium)
                Text("Autor: ${bookWithReview.book.author}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if(bookWithReview.reviews.isEmpty()) {
                    Text("Nenhuma avaliação", style = MaterialTheme.typography.bodySmall)
                } else {
                    bookWithReview.reviews.forEach { review ->
                        Text("Avaliação: ${review.rating} - ${review.comment}", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = idBook,
                onValueChange = { idBook = it },
                label = { Text("Buscar Livro por ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.searchBook(idBook.toIntOrNull() ?: 0)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Livro")
            }

            Spacer(modifier = Modifier.height(16.dp))
            foundBook?.let { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Título: ${book.title}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Autor: ${book.author}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } ?: Text("Nenhum livro encontrado ou digite um ID para buscar um Livro!")
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}