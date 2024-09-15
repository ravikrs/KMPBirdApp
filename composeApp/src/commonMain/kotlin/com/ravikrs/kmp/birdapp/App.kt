package com.ravikrs.kmp.birdapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val birdsViewModel = getViewModel(Unit, viewModelFactory { BirdsViewModel() })
        val uiState by birdsViewModel.uiState.collectAsState()
        LaunchedEffect(birdsViewModel) {
            birdsViewModel.updateImages()
        }

//        Text(uiState.images.toString(), Modifier.verticalScroll(rememberScrollState()))
        BirdsPage(uiState, onSelectCategory = { birdsViewModel.selectCategory(it) })
    }
}

@Composable
fun BirdsPage(uiState: BirdsUiState, onSelectCategory: (String) -> Unit) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        ) {
            for (category in uiState.categories) {
                Button(
                    onClick = { onSelectCategory(category) },
                    modifier = Modifier.aspectRatio(1.0f).weight(1.0f)
                ) {
                    Text(category)
                }
            }
        }
        AnimatedVisibility(visible = uiState.selectedImages.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)
            ) {
                items(uiState.selectedImages) { image ->
                    BirdImageCell(image)
                }
            }
        }
    }
}

@Composable
fun BirdImageCell(image: BirdImage) {
    KamelImage(
        resource = asyncPainterResource("https://sebi.io/demo-image-api/${image.path}"),
        contentDescription = "${image.category} by ${image.author}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
    )

}
