//
//  TeamMemberUIView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct TeamMembersUIView: View {
    var members: Array<TeamMemberItemUi>
    
    var body: some View {
        List {
            ForEach(members, id: \.id) { member in
                TeamMemberItemNavigation(teamMember: member)
            }
        }
    }
}

#Preview {
    TeamMembersUIView(
        members: [TeamMemberItemUi.companion.fake]
    )
}
