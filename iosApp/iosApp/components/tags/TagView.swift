//
//  TagView.swift
//  iosApp
//
//  Created by GERARD on 13/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct TagView: View {
    var text: String
    var icon: String?
    var containerColor: Color = Color.decorativeAmethyst
    var contentColor: Color = Color.decorativeOnAmethyst
    
    var body: some View {
        Label {
            Text(text)
                .foregroundColor(contentColor)
                .font(.caption)
        } icon: {
            if (icon != nil) {
                Image(systemName: icon!)
                    .foregroundColor(contentColor)
            }
        }
        .padding([.horizontal], 8)
        .padding([.vertical], 6)
        .background(containerColor)
        .clipShape(RoundedRectangle(cornerRadius: 16))
    }
}

struct TagView_Previews: PreviewProvider {
    static var previews: some View {
        TagView(
            text: "Mobile",
            icon: "iphone"
        )
    }
}
