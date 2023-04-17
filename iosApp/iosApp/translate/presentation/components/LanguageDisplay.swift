//
//  LanguageDisplay.swift
//  iosApp
//
//  Created by Rhea Chhabra on 17/04/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
struct LanguageDisplay: View {
    var language: UILanguage
    var body: some View {
        HStack {
            SmallLanguageIcon(language: language)
                .padding(.trailing, 5)
            Text(language.language.langName)
                .foregroundColor(.lightBlue)
        }
    }
}

struct LanguageDisplay_Previews: PreviewProvider {
    static var previews: some View {
        LanguageDisplay(language: UILanguage(language: .german, imageName: "german"))
    }
}

