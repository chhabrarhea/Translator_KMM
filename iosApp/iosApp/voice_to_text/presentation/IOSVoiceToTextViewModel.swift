//
//  IOSVoiceToTextViewModel.swift
//  iosApp
//
//  Created by Rhea Chhabra on 16/07/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

// MainActor: ensures everything happens on main thread
@MainActor class IOSVoiceToTextViewModel: ObservableObject {
    //any - any type of implementation of the interface
    private var parser: any VoiceToTextParser
    private let languageCode: String
    
    private let viewModel: VoiceToTextViewModel
    @Published var state = VoiceToTextState(powerRatios: [], spokenText: "", canRecord: false, recordError: nil, displayState: nil)
    private var handle: DisposableHandle?
    
    init(parser: VoiceToTextParser, languageCode: String){
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = VoiceToTextViewModel(parser: parser, viewModelScope: nil)
        self.viewModel.onEvent(event: VoiceToTextEvent.PermissionResult(isGranted: true, shouldShowPermissionRationale: false))
    }
    
    func onEvent(event: VoiceToTextEvent){
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.collect { [weak self] state in
            if let state {
                self?.state = state
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
        onEvent(event: VoiceToTextEvent.Reset())
    }
}
