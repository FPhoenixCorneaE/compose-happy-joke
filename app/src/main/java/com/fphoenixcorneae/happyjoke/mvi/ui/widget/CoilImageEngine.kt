package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import github.leavesczy.matisse.ImageEngine
import github.leavesczy.matisse.MediaResource
import kotlinx.parcelize.Parcelize

/**
 * @desc：
 * @date：2023/06/26 16:33
 */
@Parcelize
class CoilImageEngine : ImageEngine {
    @Composable
    override fun Image(mediaResource: MediaResource) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = mediaResource.uri,
            contentDescription = mediaResource.name,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None
        )
    }

    @Composable
    override fun Thumbnail(mediaResource: MediaResource) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = mediaResource.uri,
            contentDescription = mediaResource.name,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None
        )
    }
}