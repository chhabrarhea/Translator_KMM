//
//  GradientSurface.swift
//  iosApp
//
//  Created by Rhea Chhabra on 17/04/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GradientSurface: ViewModifier {
    @Environment(\.colorScheme) var colorScheme
    func body(content: Content) -> some View {
        if colorScheme == .dark {
            let gradientStart = Color(hex: 0xFF23262E)
            let gradientEnd = Color(hex: 0xFF212329)
            content.background(
                LinearGradient(
                    colors: [gradientStart, gradientEnd],
                    startPoint: .top,
                    endPoint: .bottom
                )
            )
        } else { content.background(Color.surface) }
    }
}

extension View {
    func applyGradientSurface() -> some View {
       modifier(GradientSurface())
    }
}
