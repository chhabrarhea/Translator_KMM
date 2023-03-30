//
//  IOSTranslateViewModel.swift
//  iosApp
//
//  Created by Rhea Chhabra on 30/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension TranslateScreen {
    @MainActor class IOSTranslateViewModel: ObservableObject {
        private var historyDataSource: HistoryDataSource
        private var translateUseCase: Translate
        
        private let viewModel: TranslateViewModel
        
        @Published var state: TranslateState = TranslateState (
            fromText: "",
            toText: nil,
            isTranslating: false,
            fromLanguage: UILanguage.Companion().allLanguages.first!,
            toLanguage: UILanguage.Companion().allLanguages.last!,
            isChoosingFromLanguage: false,
            isChoosingToLanguage: false,
            error: nil,
            history: []
        )
        
        private var handle: DisposableHandle?
        
        init(historyDataSource: HistoryDataSource, translateUseCase: Translate) {
            self.historyDataSource = historyDataSource
            self.translateUseCase = translateUseCase
            self.viewModel = TranslateViewModel(translate: translateUseCase, historyDataSource: historyDataSource, coroutineScope: nil)
        }
        
        func onEvent(event: TranslateEvent){
            viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handle = viewModel.state.collect(onCollect: { state in
                if let state = state {
                    self.state = state
                }
            })
        }
        
        func dispose() {
            handle?.dispose()
        }
    }
}
