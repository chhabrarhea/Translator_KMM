//
//  TranslateTextField.swift
//  iosApp
//
//  Created by Rhea Chhabra on 17/04/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import UniformTypeIdentifiers

struct TranslateTextField: View {
    @Binding var fromText: String
    let toText: String?
    let isTranslating: Bool
    let fromLanguage: UILanguage
    let toLanguage: UILanguage
    let onTranslateEvent: (TranslateEvent) -> Void
    
    
    var body: some View {
        if toText == nil || isTranslating {
            IdleTextField(fromText: $fromText, isTranslating: isTranslating, onTranslateEvent: onTranslateEvent)
                .applyGradientSurface()
                .cornerRadius(15)
                .animation(.easeInOut, value: isTranslating)
                .shadow(radius: 4)
        }
        else {
            TranslatedTextField(
                fromLanguage: fromLanguage,
                toLanguage: toLanguage,
                fromText: fromText,
                toText: toText ?? "",
                onTranslateEvent: onTranslateEvent
            )
            .padding()
            .applyGradientSurface()
            .cornerRadius(15)
            .animation(.easeInOut, value: isTranslating)
            .shadow(radius: 4)
            .onTapGesture {
                onTranslateEvent(TranslateEvent.EditTranslation())
            }
        }
    }
}



private extension TranslateTextField {
    
    struct IdleTextField: View {
        @Binding var fromText: String
        let isTranslating: Bool
        let onTranslateEvent: (TranslateEvent) -> Void
        
        var body: some View {
            TextEditor(text: $fromText)
                .frame(maxWidth: .infinity, minHeight: 200, alignment: .topLeading)
                .padding()
                .foregroundColor(Color.onSurface)
                .overlay(alignment: .bottomTrailing){
                    ProgressButton(
                        text: "Translate",
                        isLoading: isTranslating,
                        onClick: { onTranslateEvent(TranslateEvent.Translate()) }
                    )
                    .padding(.trailing)
                    .padding(.bottom)
                }
                .onAppear { UITextView.appearance().backgroundColor = .clear }
        }
    }
    
    struct TranslatedTextField: View {
        let fromLanguage: UILanguage
        let toLanguage: UILanguage
        let fromText: String
        let toText: String
        let onTranslateEvent: (TranslateEvent) -> Void
        
        var body: some View {
            VStack(alignment: .leading) {
                LanguageDisplay(language: fromLanguage)
                Text(fromText)
                    .foregroundColor(.onSurface)
                HStack {
                    Spacer()
                    Button(action: {
                        UIPasteboard.general.setValue(fromText, forPasteboardType: UTType.plainText.identifier)
                    }) {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    Button(action: {
                        onTranslateEvent(TranslateEvent.CloseTranslation())
                    }) {
                        Image(systemName: "xmark")
                            .foregroundColor(.lightBlue)
                    }
                }
                Divider()
                    .padding()
                LanguageDisplay(language: toLanguage)
                    .padding(.bottom)
                Text(toText)
                    .foregroundColor(.onSurface)
                
                HStack {
                    Spacer()
                    Button(action: {
                        UIPasteboard.general.setValue(
                            toText,
                            forPasteboardType: UTType.plainText.identifier
                        )
                    }) {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    Button(action: {
                        
                    }) {
                        Image(systemName: "speaker.wave.2")
                            .foregroundColor(.lightBlue)
                    }
                }
            }
        }
    }
    
}
