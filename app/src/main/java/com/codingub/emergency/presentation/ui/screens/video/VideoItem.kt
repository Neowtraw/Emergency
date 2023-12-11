package com.codingub.emergency.presentation.ui.screens.video

import android.net.Uri
import androidx.media3.common.MediaItem

data class VideoItem(
    val uri: Uri,
    val mediaItem: MediaItem
)
