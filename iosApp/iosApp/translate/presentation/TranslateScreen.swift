//
//  TranslateScreen.swift
//  iosApp
//
//  Created by Rhea Chhabra on 30/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TranslateScreen: View {
    private var historyDataSource: HistoryDataSource
    private var translateUseCase: Translate
    @ObservedObject var viewModel: IOSTranslateViewModel
    
    init(historyDataSource: HistoryDataSource, translateUseCase: Translate) {
        self.historyDataSource = historyDataSource
        self.translateUseCase = translateUseCase
        self.viewModel = IOSTranslateViewModel(historyDataSource: historyDataSource, translateUseCase: translateUseCase)
    }
    
    var body: some View {
        ZStack {
            List {
                HStack {
                    LanguageDropDown(
                        selectedLanguage: viewModel.state.fromLanguage,
                        onLanguageSelected: { language in viewModel.onEvent(event: TranslateEvent.ChooseFromLanguage(language: language))},
                        isOpen: viewModel.state.isChoosingFromLanguage)
                    Spacer()
                    SwapLanguageButton(onClick: {viewModel.onEvent(event: TranslateEvent.SwapLanguages())})
                    Spacer()
                    LanguageDropDown(
                        selectedLanguage: viewModel.state.toLanguage,
                        onLanguageSelected: { language in viewModel.onEvent(event: TranslateEvent.ChooseToLanguage(language: language))},
                        isOpen: viewModel.state.isChoosingToLanguage)
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                TranslateTextField(
                    fromText: Binding(get: { viewModel.state.fromText }, set: { value in
                        viewModel.onEvent(event: TranslateEvent.ChangeTranslationText(text: value))
                    }),
                    toText: viewModel.state.toText,
                    isTranslating: viewModel.state.isTranslating,
                    fromLanguage: viewModel.state.fromLanguage,
                    toLanguage: viewModel.state.toLanguage,
                    onTranslateEvent: { viewModel.onEvent(event: $0) }
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
            }
            .listStyle(.plain)
            .buttonStyle(.plain)
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

