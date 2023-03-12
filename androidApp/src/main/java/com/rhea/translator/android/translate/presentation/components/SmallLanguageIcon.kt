package com.rhea.translator.android.translate.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rhea.translator.presentation.UILanguage

@Composable
fun SmallLanguageIcon(language: UILanguage, modifier: Modifier = Modifier) {
    AsyncImage(
        model = language.flagRes,
        contentDescription = language.language.langName,
        modifier = modifier.size(25.dp)
    )
}