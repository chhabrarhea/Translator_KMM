//
//  TranslateScreen.swift
//  iosApp
//
//  Created by Rhea Chhabra on 30/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

/// <#Description#>dd
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
                    onTranslateEvent: { event in
                        viewModel.onEvent(event: event)
                    }
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                if !viewModel.state.history.isEmpty {
                    Text("History")
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.background)
                }
                
                ForEach(viewModel.state.history, id: \.id){ item in
                    TranslateHistoryItem(
                        item: item,
                        onClick: { viewModel.onEvent(event: TranslateEvent.SelectHistoryItem(item: item))}
                    )
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.background)
                }
            }
            .listStyle(.plain)
            .buttonStyle(.plain)
            
            VStack {
                Spacer()
                NavigationLink(
                    destination: VoiceToTextScreen(
                        onResult: { spokenText in
                            viewModel.onEvent(event: TranslateEvent.SubmitVoiceResult(result: spokenText))
                        },
                        parser: IOSVoiceToTextParser(),
                        languageCode: viewModel.state.fromLanguage.language.langCode
                    )
                ){
                    ZStack {
                        Circle()
                            .foregroundColor(.primaryColor)
                            .padding()
                        Image(uiImage: UIImage.init(named: "mic")!)
                            .foregroundColor(.onPrimary)
                    }
                    .frame(maxWidth: 100, maxHeight: 100)
                }
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

