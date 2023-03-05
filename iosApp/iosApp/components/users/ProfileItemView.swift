//
//  ProfileItemView.swift
//  iosApp
//
//  Created by GERARD on 05/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProfileItemView: View {
    let profileUi: UserProfileUi

    var body: some View {
        VStack(alignment: .leading,spacing: 8) {
            Text("\(profileUi.firstName) \(profileUi.lastName)")
            if (profileUi.company != "") {
                Label(profileUi.company, systemImage: "suitcase")
                    .font(.callout)
                    .foregroundColor(.secondary)
            }
            Label(profileUi.email, systemImage: "envelope")
                .font(.callout)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct ProfileItemView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileItemView(
            profileUi: UserProfileUi.companion.fake
        )
    }
}
