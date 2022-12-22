//
//  CardModifier.swift
//  iosApp
//
//  Created by GERARD on 22/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CardModifier: ViewModifier {
    var radius: CGFloat = 4
    var elevation: CGFloat = 0
    
    func body(content: Content) -> some View {
        content
            .cornerRadius(self.radius)
            .shadow(color: Color.black.opacity(0.2), radius: self.elevation, x: 0, y: 0)
    }
}

struct CardModifier_Previews: PreviewProvider {
    static var previews: some View {
        HStack {
            Text("My card")
                .foregroundColor(Color.c4hOnSurface)
                .padding()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.c4hSurface)
        .modifier(CardModifier())
        .padding(.all, 10)
    }
}
