//
//  LanguageDropDown.swift
//  iosApp
//
//  Created by Rhea Chhabra on 29/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LanguageDropDown: View {
    var selectedLanguage: UILanguage
    var onLanguageSelected: (UILanguage) -> Void
    var isOpen: Bool
    var body: some View {
        Menu {
            VStack {
                ForEach(UILanguage.Companion().allLanguages, id: \.self.language.langCode) { language in
                    LanguageDropDownItem(
                        language: language,
                        onClick: { onLanguageSelected(selectedLanguage)}
                    )
                }
            }
        } label: {
            HStack {
                SmallLanguageIcon(language: selectedLanguage)
                Text(selectedLanguage.language.langName)
                                    .foregroundColor(.lightBlue)
                                Image(systemName: isOpen ? "chevron.up" : "chevron.down")
                                    .foregroundColor(.lightBlue)
            }
        }
    }
}

struct LanguageDropDown_Previews: PreviewProvider {
    static var previews: some View {
        LanguageDropDown(
            selectedLanguage: UILanguage.Companion().allLanguages.first!,
            onLanguageSelected: { lang in },
            isOpen: false
        )
    }
}
