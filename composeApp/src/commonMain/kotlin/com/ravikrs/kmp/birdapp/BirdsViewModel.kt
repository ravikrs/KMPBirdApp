package com.ravikrs.kmp.birdapp

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BirdsUiState(val images: List<BirdImage>, val selectedCategory: String? = null) {
    val categories = images.map { it.category }.toSet()
    val selectedImages = images.filter { it.category == selectedCategory }
}

class BirdsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BirdsUiState(emptyList()))
    val uiState = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(images = images)
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            if (state.selectedCategory == category) {
                state.copy(selectedCategory =  null)
            } else {
                state.copy(selectedCategory = category)
            }
        }
    }


    override fun onCleared() {
        httpClient.close()
    }

    private suspend fun getImages(): List<BirdImage> =
        httpClient.get("https://sebi.io/demo-image-api/pictures.json")
            .body<List<BirdImage>>()
}