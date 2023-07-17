//
//  IOSVoiceToTextParser.swift
//  iosApp
//
//  Created by Rhea Chhabra on 15/07/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine
import Speech

class IOSVoiceToTextParser: VoiceToTextParser, ObservableObject {
    
    private let _state = IOSMutableStateFlow(initialValue: VoiceToTextParserState(
        result: "",
        error: nil,
        powerRatio: 0.0,
        isSpeaking: false
    ))
    
    var state: CommonStateFlow<VoiceToTextParserState>{ _state } // getter will return value of _state
    
    private var micObserver = MicrophonePowerObserver()
    var micPowerRatio: Published<Double>.Publisher {
        micObserver.$micPowerRatio
    }
    private var micPowerCancellable: AnyCancellable?
    
    private var recognizer : SFSpeechRecognizer?
    private var audioEngine: AVAudioEngine?
    private var inputNode: AVAudioInputNode?  // Audio goes in
    private var audioBufferRequest: SFSpeechAudioBufferRecognitionRequest?
    private var audioSession: AVAudioSession?
    private var recognitionTask: SFSpeechRecognitionTask?
    
    func cancel() {
       //NOT NEEDED IN IOS
    }
    
    func reset() {
        self.stopListening()
        _state.value = VoiceToTextParserState(
            result: "",
            error: nil,
            powerRatio: 0.0,
            isSpeaking: false
        )
    }
    
    func startListening(languageCode: String) {
        updateState(error: nil)
        
        let chosenLocale = Locale.init(identifier: languageCode)
        let supportedLocale = SFSpeechRecognizer.supportedLocales().contains(chosenLocale) ? chosenLocale : Locale.init(identifier:"en-US")
        self.recognizer = SFSpeechRecognizer(locale: supportedLocale)
        
        // guard -> assertion
        guard recognizer?.isAvailable == true else {
            updateState(error: "Speech Recognizer is not available")
            return
        }
        
        audioSession = AVAudioSession.sharedInstance()
        self.requestPermissions { [weak self] in
            self?.audioBufferRequest = SFSpeechAudioBufferRecognitionRequest()
            guard let audioBufferRequest = self?.audioBufferRequest else { return }
            self?.recognitionTask = self?.recognizer?.recognitionTask(with: audioBufferRequest) { [weak self] (result, error) in
                guard let resultant = result else {
                    self?.updateState(error: error?.localizedDescription)
                    return
                }
                if resultant.isFinal {
                    self?.updateState(result: resultant.bestTranscription.formattedString)
                }
            }
            self?.audioEngine = AVAudioEngine()
            self?.inputNode = self?.audioEngine?.inputNode
            
        
            let recordingFormat = self?.inputNode?.outputFormat(forBus: 0)
            self?.inputNode?.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
                self?.audioBufferRequest?.append(buffer)
            }
            
            self?.audioEngine?.prepare()
            
            do {
                // what kind of audio recognition this is; mode -> optimize UX; duckOthers -> reduce volume of other audio sessions
                try self?.audioSession?.setCategory(.playAndRecord, mode: .spokenAudio, options: .duckOthers)
                
                try self?.audioSession?.setActive(true, options: .notifyOthersOnDeactivation)
                
                self?.micObserver.startObserving()
                            
                try self?.audioEngine?.start()
                            
                self?.updateState(isSpeaking: true)
                            
                self?.micPowerCancellable = self?.micPowerRatio
                    .sink { [weak self] ratio in
                        self?.updateState(powerRatio: ratio)
                    }
            } catch {
                self?.updateState(error: error.localizedDescription, isSpeaking: false)
            }
        }
    }
    
    func stopListening() {
        self.updateState(isSpeaking: false)
        micPowerCancellable = nil
        micObserver.release()
        audioBufferRequest?.endAudio()
        audioBufferRequest = nil
        audioEngine?.stop()
        inputNode?.removeTap(onBus: 0)
        try? audioSession?.setActive(false) // try? ignore exception
        audioSession = nil
    }
    
    private func updateState(result: String? = nil, error: String? = nil, powerRatio: CGFloat? = nil, isSpeaking: Bool? = false) {
        let currentState = _state.value
        _state.value = VoiceToTextParserState(
            result: result ?? currentState?.result ?? "",
            error: error ?? currentState?.error,
            powerRatio: Float(powerRatio ?? CGFloat(currentState?.powerRatio ?? 0.0)),
            isSpeaking: isSpeaking ?? currentState?.isSpeaking ?? false
        )
    }
    
    private func requestPermissions(onGranted: @escaping () -> Void) {
        audioSession?.requestRecordPermission {[weak self] wasGranted in
            if !wasGranted {
                self?.updateState(error: "Permission to record your voice is mandatory")
                self?.stopListening()
                return
            }
        }
        SFSpeechRecognizer.requestAuthorization {[weak self] status in
            DispatchQueue.main.async {
                if status != .authorized {
                    self?.updateState(error: "Permission to transcribe audio is mandatory")
                    self?.stopListening()
                    return
                }
            }
        }
        onGranted()
    }
}

