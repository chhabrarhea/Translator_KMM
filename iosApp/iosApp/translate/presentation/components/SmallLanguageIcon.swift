//
//  SmallLanguageIcon.swift
//  iosApp
//
//  Created by Rhea Chhabra on 29/03/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SmallLanguageIcon: View {
    var language: UILanguage
    var body: some View {
        if let image = UIImage(named: language.imageName){
            Image(uiImage: image)
                .resizable()
                .frame(width: 30, height: 30)
        }
    }
}

struct SmallLanguageIcon_Previews: PreviewProvider {
    static var previews: some View {
        SmallLanguageIcon(language: UILanguage.Companion().allLanguages.first!)
    }
}
