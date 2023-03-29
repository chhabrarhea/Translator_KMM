//
//  LanguageDropDownItem.swift
//  iosApp
//
//  Created by Rhea Chhabra on 29/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LanguageDropDownItem: View {
    var language: UILanguage
    var onClick: () -> Void
    var body: some View {
        Button(action: onClick){
            HStack {
                if let image = UIImage(named: language.imageName.lowercased()) {
                    Image(uiImage: image)
                        .resizable()
                        .frame(width: 40, height: 40)
                        .padding(.trailing, 5)
                    Text(language.language.langName)
                        .foregroundColor(.textBlack)
                }
            }
        }
    }
}

struct LanguageDropDownItem_Previews: PreviewProvider {
    static var previews: some View {
        LanguageDropDownItem(
            language: UILanguage(language: .german, imageName: "german"),
            onClick: {}
        )
    }
}
