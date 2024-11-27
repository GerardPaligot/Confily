//
//  ScheduleEventSessionDetail.swift
//  iosApp
//
//  Created by GERARD on 10/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct ScheduleEventSessionDetail: View {
    let session: EventSessionUi
    var color: Color = Color.c4hOnBackground
    var titleFont: Font = Font.title
    var bodyFont: Font = Font.callout
    
    var body: some View {
        List {
            VStack(alignment: .leading) {
                VStack(alignment: .leading, spacing: 24) {
                    Text(session.title)
                        .font(titleFont)
                        .padding(.top, 8)
                    HStack {
                        TagUnStyledView(
                            text: session.slotTime,
                            icon: "deskclock"
                        )
                        TagUnStyledView(
                            text: session.room,
                            icon: "mappin"
                        )
                        TagUnStyledView(
                            text: "\(session.timeInMinutes) minutes",
                            icon: session.timeInMinutes <= 30 ? "bolt.badge.clock" : "clock"
                        )
                    }
                }
                .accessibilityElement(children: .combine)
                Text(.init(session.description_))
                    .foregroundColor(color.opacity(0.74))
                    .padding(.top, 8)
            }
            .frame(maxWidth: .infinity, alignment: .topLeading)
            .foregroundColor(color)
            .font(bodyFont)
            if let addressUi = session.addressUi {
                AddressCardView(formattedAddress: addressUi.formattedAddress)
                Link("actionItinerary", destination: URL(string: "maps://?saddr=&daddr=\(addressUi.latitude),\(addressUi.longitude)")!)
            }
        }
        .listStyle(.plain)
    }
}
