//
//  PartnerDividerView.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PartnerDividerView: View {
    var text: String
    var dividerColor: Color = Color.c4hPrimary
    var titleColor: Color = Color.c4hSecondary
    var font: Font = Font.subheadline
    
    var body: some View {
        HStack {
            Text(text)
                .foregroundColor(titleColor)
                .font(font)
            VStack { Divider().background(dividerColor) }
        }
        .padding(.top, 8)
        .padding(.bottom, 8)
    }
}

struct PartnerDividerView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDividerView(text: "Community Partners")
    }
}
