//
//  TagUnStyledView.swift
//  iosApp
//
//  Created by GERARD on 13/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct TagUnStyledView: View {
    var text: String
    var icon: String?

    var body: some View {
        TagView(
            text: text,
            icon: icon,
            containerColor: Color.white.opacity(0),
            contentColor: Color.c4hOnBackground
        )
    }
}

struct TagUnStyledView_Previews: PreviewProvider {
    static var previews: some View {
        TagUnStyledView(
            text: "50 minutes",
            icon: "clock"
        )
    }
}
