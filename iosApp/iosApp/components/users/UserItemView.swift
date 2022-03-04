//
//  UserItemView.swift
//  iosApp
//
//  Created by GERARD on 04/03/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct UserItemView: View {
    var user: UserNetworkingUi
    var color: Color = Color.c4hOnBackground
    var nameFont: Font = Font.body
    var metaFont: Font = Font.callout
    
    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            HStack(spacing: 2) {
                Text(user.firstName)
                Text(user.lastName)
            }
            .foregroundColor(color)
            .font(nameFont)
            Text(user.email)
            Text(user.company)
        }
        .foregroundColor(color.opacity(0.74))
        .font(metaFont)
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct UserItemView_Previews: PreviewProvider {
    static var previews: some View {
        UserItemView(
            user: UserNetworkingUi(
                email: "gerard@gdglille.org",
                firstName: "Gerard",
                lastName: "Pal",
                company: "Decathlon"
            )
        )
    }
}
