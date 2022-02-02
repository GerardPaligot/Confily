package com.paligot.conferences.ui.components.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Dialog

@Composable
fun QrCodeDialog(
    email: String,
    imageBitmap: ImageBitmap,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Email of the qrcode specified is $email",
                modifier = Modifier.wrapContentSize()
            )
        }
    )
}
