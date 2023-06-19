package com.rhea.translator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rhea.translator.android.MainActivity.Companion.LANGUAGE_CODE
import com.rhea.translator.android.MainActivity.Companion.VOICE_RESULT
import com.rhea.translator.android.core.presentation.Routes
import com.rhea.translator.android.core.theme.TranslatorTheme
import com.rhea.translator.android.translate.presentation.AndroidTranslateViewModel
import com.rhea.translator.android.translate.presentation.TranslateScreen
import com.rhea.translator.android.voice_to_text.presentation.VoiceToTextScreen
import com.rhea.translator.android.voice_to_text.presentation.viewmodel.AndroidVoiceToTextViewModel
import com.rhea.translator.presentation.TranslateEvent
import com.rhea.translator.voice_to_text.VoiceToTextEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TranslatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TranslateRoot()
                }
            }
        }
    }

    companion object {
        const val LANGUAGE_CODE = "languageCode"
        const val DEFAULT_LANGUAGE_CODE = "en"
        const val VOICE_RESULT = "voiceResult"
    }
}

@Composable
fun TranslateRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.TRANSLATE
    ) {
        composable(route = Routes.TRANSLATE) {
            val viewModel = hiltViewModel<AndroidTranslateViewModel>()
            val state by viewModel.state.collectAsState()

            val voiceResult by it
                .savedStateHandle.getStateFlow<String?>(VOICE_RESULT, null)
                .collectAsState()

            LaunchedEffect(voiceResult){
                viewModel.onEvent(TranslateEvent.SubmitVoiceResult(voiceResult))
                it.savedStateHandle[VOICE_RESULT] = null
            }
            TranslateScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TranslateEvent.RecordAudio -> {
                            navController.navigate(
                                Routes.VOICE_TO_TEXT + "/${state.fromLanguage.language.langCode}"
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }
        composable(
            route = Routes.VOICE_TO_TEXT + "/{$LANGUAGE_CODE}",
            arguments = listOf(
                navArgument(LANGUAGE_CODE) {
                    type = NavType.StringType
                    defaultValue = MainActivity.DEFAULT_LANGUAGE_CODE
                }
            )
        ) {
            val langCode = it.arguments?.getString(LANGUAGE_CODE) ?: MainActivity.DEFAULT_LANGUAGE_CODE
            val viewModel: AndroidVoiceToTextViewModel by  hiltViewModel()
            val state by viewModel.state.collectAsState()

            VoiceToTextScreen(
                state = state,
                languageCode = langCode,
                onResult = { spokenText ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(VOICE_RESULT, spokenText)
                    navController.popBackStack()
                },
                onEvent = { event ->
                    if (event is VoiceToTextEvent.Close)
                        navController.popBackStack()
                    else viewModel.onEvent(event)
                }
            )
        }
    }
}
