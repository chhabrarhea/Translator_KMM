//
//  TranslateHistoryItem.swift
//  iosApp
//
//  Created by Rhea Chhabra on 26/04/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TranslateHistoryItem: View {
    let item: UiHistoryItem
    let onClick: () -> Void
    
    var body: some View {
        Button(action: onClick){
            VStack(alignment: .leading, spacing: 20) {
                HStack {
                    SmallLanguageIcon(language: item.fromLanguage)
                        .padding(.trailing)
                    Text(item.fromText)
                        .foregroundColor(.lightBlue)
                        .font(.body)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.bottom)
                HStack {
                    SmallLanguageIcon(language: item.toLanguage)
                        .padding(.trailing)
                    Text(item.toText)
                        .foregroundColor(.onSurface)
                        .font(.body.weight(.semibold))
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .frame(maxWidth: .infinity)
            .padding()
            .applyGradientSurface()
            .cornerRadius(15)
            .shadow(radius: 5)
        }
    }
}

struct TranslateHistoryItem_Previews: PreviewProvider {
    static var previews: some View {
        TranslateHistoryItem(
            item: UiHistoryItem(id: 1, fromLanguage: UILanguage(language: .german, imageName: "german"), toLanguage: UILanguage(language: .english, imageName: "english"), fromText: "Hello", toText: "Hello"), onClick: {}
        )
    }
}
