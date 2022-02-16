//
//  ButtonView.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ButtonView: View {
    var text: String
    var backgroundColor: Color = Color.c4hPrimary
    var contentColor: Color = Color.c4hOnPrimary
    var onClick: () -> ()

    var body: some View {
        Button(action: onClick) {
            Text(text)
                .frame(minWidth: 0, maxWidth: .infinity)
                .padding(.vertical, 12)
                .foregroundColor(contentColor)
                .background(backgroundColor)
                .cornerRadius(8)
        }
    }
}

struct ButtonView_Previews: PreviewProvider {
    static var previews: some View {
        ButtonView(text: "FAQ") {
        }
    }
}
