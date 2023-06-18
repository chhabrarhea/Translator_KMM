package com.rhea.translator.android.voice_to_text.presentation

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rhea.translator.voice_to_text.DisplayState
import com.rhea.translator.voice_to_text.VoiceToTextEvent
import com.rhea.translator.voice_to_text.VoiceToTextState
import com.rhea.translator.android.R
import com.rhea.translator.android.core.theme.LightBlue
import com.rhea.translator.android.voice_to_text.presentation.components.VoiceRecorderDisplay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VoiceToTextScreen(
    state: VoiceToTextState,
    languageCode: String,
    onResult: (String) -> Unit,
    onEvent: (VoiceToTextEvent) -> Unit
) {
    val audioPermission = Manifest.permission.RECORD_AUDIO
    val activity = (LocalContext.current as ComponentActivity)
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onEvent.invoke(
                VoiceToTextEvent.PermissionResult(
                    isGranted,
                    activity.shouldShowRequestPermissionRationale(audioPermission)
                )
            )
        }
    )

    /**
     * Launch the block when LaunchedEffect enters composition
     * It will cancel and relaunch block when key1 is changed
     * LaunchedEffect vs rememberCoroutineScope ?
     */
    LaunchedEffect(key1 = recordAudioLauncher){
        recordAudioLauncher.launch(audioPermission)
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FloatingActionButton(
                    onClick = {
                        if (state.displayState != DisplayState.DISPLAYING_RESULTS)
                            onEvent.invoke(VoiceToTextEvent.ToggleRecording(languageCode))
                        else onResult.invoke(state.spokenText)
                },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(75.dp)
                ) {
                    AnimatedContent(targetState = state.displayState) {
                        when(it){
                            DisplayState.SPEAKING -> {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(id = R.string.stop_recording),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            DisplayState.DISPLAYING_RESULTS -> {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(id = R.string.apply),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable .mic),
                                    contentDescription = stringResource(id = R.string.record_audio),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                    if (state.displayState == DisplayState.DISPLAYING_RESULTS) {
                        IconButton(onClick = {
                            onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = stringResource(id = R.string.record_again),
                                tint = LightBlue
                            )
                        }
                    }

                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)){
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onEvent(VoiceToTextEvent.Close)
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
                if(state.displayState == DisplayState.SPEAKING) {
                    Text(
                        text = stringResource(id = R.string.listening),
                        color = LightBlue,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(targetState = state.displayState) { displayState ->
                    when(displayState) {
                        DisplayState.WAITING_TO_TALK -> {
                            Text(
                                text = stringResource(id = R.string.start_talking),
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center
                            )
                        }
                        DisplayState.SPEAKING -> {
                            VoiceRecorderDisplay(
                                powerRatios = state.powerRatios,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                        }
                        DisplayState.DISPLAYING_RESULTS -> {
                            Text(
                                text = state.spokenText,
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center
                            )
                        }
                        DisplayState.ERROR -> {
                            Text(
                                text = state.recordError ?: "Unknown error",
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.error
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
