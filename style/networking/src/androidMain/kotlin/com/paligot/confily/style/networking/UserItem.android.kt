package com.paligot.confily.style.networking

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun UserItemPreview() {
    Conferences4HallTheme {
        Surface {
            Column {
                UserItem(
                    displayName = "Gerard Paligot",
                    email = "gerard@gmail.com",
                    company = "Decathlon",
                    onClick = {}
                )
                Divider(color = MaterialTheme.colorScheme.onBackground)
                UserItem(
                    displayName = "Gerard Paligot",
                    email = "gerard@gmail.com",
                    company = null,
                    onClick = {}
                )
            }
        }
    }
}
