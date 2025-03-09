//
//  LineView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 09/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct Line: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: 0, y: 0))
        path.addLine(to: CGPoint(x: rect.width, y: 0))
        return path
    }
}
