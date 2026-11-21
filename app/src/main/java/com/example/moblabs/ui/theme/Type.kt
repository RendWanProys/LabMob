package com.example.moblabs.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moblabs.R

val MichromaFont = FontFamily(
    Font(R.font.michroma, FontWeight.Normal),
    Font(R.font.michroma, FontWeight.Bold),
    Font(R.font.michroma, FontWeight.Medium)
)

val defaultStyle = TextStyle(
    fontFamily = MichromaFont,
    fontWeight = FontWeight.Normal
)

val Typography = Typography(
    displayLarge = defaultStyle.copy(fontSize = 57.sp),
    displayMedium = defaultStyle.copy(fontSize = 45.sp),
    displaySmall = defaultStyle.copy(fontSize = 36.sp),
    headlineLarge = defaultStyle.copy(fontSize = 32.sp),
    headlineMedium = defaultStyle.copy(fontSize = 28.sp),
    headlineSmall = defaultStyle.copy(fontSize = 24.sp),
    titleLarge = defaultStyle.copy(fontSize = 22.sp),
    titleMedium = defaultStyle.copy(fontSize = 16.sp),
    titleSmall = defaultStyle.copy(fontSize = 14.sp),
    bodyLarge = defaultStyle.copy(fontSize = 16.sp),
    bodyMedium = defaultStyle.copy(fontSize = 14.sp),
    bodySmall = defaultStyle.copy(fontSize = 12.sp),
    labelLarge = defaultStyle.copy(fontSize = 14.sp),
    labelMedium = defaultStyle.copy(fontSize = 12.sp),
    labelSmall = defaultStyle.copy(fontSize = 11.sp)
)
