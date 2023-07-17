//
//  VoiceRecorderDisplay.swift
//  iosApp
//
//  Created by Rhea Chhabra on 16/07/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceRecorderDisplay: View {
    var powerRatios : [Double]
    var body: some View {
        Canvas { context , size in
            // create a rectangle
            context.clip(to: Path(CGRect(origin: .zero, size: size)))
            let barWidth = 3.0
            let barCount = Int(size.width / Double(2 * barWidth))
            let defaultLevel = 0.05
            let reversedRatios = powerRatios
                .map {ratio in
                    //clamp value between 0.05 & 1.0
                    min(max(defaultLevel, ratio), 1.0)
                }
                .suffix(barCount) // take last
                .reversed()
            
            for(i, powerRatio) in reversedRatios.enumerated() {
                let centerY = CGFloat(size.height / 2.0)
                let yTopStart = CGFloat(centerY - centerY * powerRatio)
                var path = Path()
                path.addRoundedRect(
                    in: CGRect(
                        x: CGFloat(size.width) - CGFloat(i) * 2.0 * barWidth,
                        y: yTopStart,
                        width: barWidth,
                        height: (centerY - yTopStart) * 2.0
                    ),
                    cornerSize: CGSize(width: 10.0, height: 10.0)
                )
                context.fill(path, with: .color(.primaryColor))
            }
        }
        .applyGradientSurface()
        .cornerRadius(20)
        .padding(.horizontal, 15)
        .padding(.vertical, 5)
        .shadow(radius: 4)
    }
}
