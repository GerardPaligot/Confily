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
    var members: Dictionary<String, Array<TeamMemberItemUi>>
    
    var body: some View {
        List {
            ForEach(Array(members.keys), id: \.self) { key in
                Section(header: Text(key)) {
                    let members = members[key] ?? []
                    ForEach(members, id: \.id) { member in
                        TeamMemberItemNavigation(teamMember: member)
                    }
                }
            }
        }
    }
}

#Preview {
    TeamMembersUIView(
        members: [
            "default": [TeamMemberItemUi.companion.fake]
        ]
    )
}
