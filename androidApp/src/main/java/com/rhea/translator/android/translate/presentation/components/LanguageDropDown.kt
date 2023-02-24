package com.rhea.translator.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rhea.translator.android.R
import com.rhea.translator.android.core.theme.LightBlue
import com.rhea.translator.presentation.Colors
import com.rhea.translator.presentation.UILanguage

@Composable
fun LanguageDropDown(
    selectedLanguage: UILanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectLanguage: (UILanguage) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        DropdownMenu(expanded = isOpen, onDismissRequest = onDismiss) {
            UILanguage.allLanguages.forEach { language ->
                LanguageDropDownItem(
                    language = language,
                    onClick = { onSelectLanguage.invoke(language) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onClick.invoke() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = selectedLanguage.flagRes,
                    contentDescription = selectedLanguage.language.langName,
                    modifier = Modifier.size(30.dp)
                )
                Text(text = selectedLanguage.language.langName, color = LightBlue)
                Icon(
                    imageVector = if (!isOpen) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    contentDescription = if (isOpen) {
                        stringResource(id = R.string.close)
                    } else {
                        stringResource(id = R.string.open)
                    },
                    tint = LightBlue,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}