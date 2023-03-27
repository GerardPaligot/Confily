package org.gdglille.devfest.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.gdglille.devfest.android.ui.resources.R

val family = FontFamily(
    Font(R.font.archivo_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(R.font.archivo_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.archivo_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.archivo_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.archivo_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.archivo_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.archivo_semi_bold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(R.font.archivo_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.archivo_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.archivo_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.archivo_black, weight = FontWeight.Black, style = FontStyle.Normal),
    Font(R.font.archivo_black_italic, weight = FontWeight.Black, style = FontStyle.Italic)
)

val typography = Typography()
