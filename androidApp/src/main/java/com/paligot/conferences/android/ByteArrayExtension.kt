package com.paligot.conferences.android

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ByteArray.asImageBitmap(): ImageBitmap = BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
