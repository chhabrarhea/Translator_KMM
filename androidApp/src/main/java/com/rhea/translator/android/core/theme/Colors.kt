package com.rhea.translator.android.core.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.rhea.translator.presentation.Colors

val AccentColor = Color(Colors.AccentViolet)
val LightBlue = Color(Colors.LightBlue)
val LightBlueGray = Color(Colors.LightBlueGrey)
val TextBlack = Color(Colors.TextBlack)
val DarkGray = Color(Colors.DarkGrey)

val lightColors = lightColors(
    primary = AccentColor,
    background = LightBlueGray,
    onPrimary = Color.White,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack
)

val darkColors = darkColors(
    primary = AccentColor,
    background = DarkGray,
    onPrimary = Color.White,
    onBackground = Color.White,
    surface = DarkGray,
    onSurface = Color.White
)