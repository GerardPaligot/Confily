//
//  SocialItem.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct SocialItem: View {
    var iconName: String
    var text: String
    var font: Font = Font.caption
    var color: Color = Color.c4hOnBackground
    var onClick: () -> ()

    var body: some View {
        Button {
            onClick()
        } label: {
            HStack {
                Image(iconName)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 24, height: 24, alignment: .center)
                    .foregroundColor(color)
                Text(text)
            }
            .foregroundColor(color)
            .font(font)
            .padding()
        }
    }
}

struct SocialItem_Previews: PreviewProvider {
    static var previews: some View {
        SocialItem(
            iconName: "ic_twitter",
            text: "@GerardPaligot",
            onClick: {}
        )
            .preferredColorScheme(.dark)
    }
}
