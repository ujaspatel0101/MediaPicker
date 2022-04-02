package com.uspatel.mediapicker

import android.graphics.Bitmap
import android.net.Uri

data class Media(
    val id: Long,
    val name: String,
    val dateAdded: String,
    val mimeType: String,
    val size: Int,
    val contentUri: Uri,
    val bitmap: Bitmap?,
    var isChecked : Boolean = false
)
