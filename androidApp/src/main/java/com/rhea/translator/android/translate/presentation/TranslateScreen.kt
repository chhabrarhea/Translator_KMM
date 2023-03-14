package com.rhea.translator.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.rhea.translator.android.R
import com.rhea.translator.android.translate.presentation.components.LanguageDropDown
import com.rhea.translator.android.translate.presentation.components.TranslateHistoryItem
import com.rhea.translator.android.translate.presentation.components.TranslateTextField
import com.rhea.translator.android.translate.presentation.components.rememberTextToSpeech
import com.rhea.translator.domain.remote.TranslateError
import com.rhea.translator.presentation.TranslateEvent
import com.rhea.translator.presentation.TranslateState
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.error) {
        val message = when(state.error) {
            TranslateError.NETWORK_ERROR -> context.getString(R.string.error_service_unavailable)
            TranslateError.CLIENT_ERROR -> context.getString(R.string.client_error)
            TranslateError.SERVER_ERROR -> context.getString(R.string.server_error)
            TranslateError.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
            else -> null
        }
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LanguageDropDown(
                        selectedLanguage = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = { onEvent.invoke(TranslateEvent.OpenFromLanguageDropDown) },
                        onDismiss = { onEvent.invoke(TranslateEvent.StopChoosingLanguage) },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseFromLanguage(language))
                        }
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    LanguageDropDown(
                        selectedLanguage = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
                        onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseToLanguage(language))
                        }
                    )
                }
            }
            item {
                val tts = rememberTextToSpeech()
                val clipboardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onTextChange = { text ->
                        onEvent(TranslateEvent.ChangeTranslationText(text))
                    },
                    onCopyClick = { text ->
                        clipboardManager.setText(buildAnnotatedString { append(text) })
                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.copied_to_clipboard
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCloseClick = {
                        onEvent(TranslateEvent.CloseTranslation)
                    },
                    onSpeakerClick = {
                        tts.language = state.toLanguage.toLocale() ?: Locale.ENGLISH
                        tts.speak(
                            state.toText,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    },
                    onTextFieldClick = {
                        onEvent(TranslateEvent.EditTranslation)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                if (state.history.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            id = R.string.history
                        ),
                        style = MaterialTheme.typography.h2
                    )
                }
            }
            items(state.history) { item ->
                TranslateHistoryItem(
                    item = item,
                    onClick = { onEvent(TranslateEvent.SelectHistoryItem(item)) })
            }
        }
    }
}