//
//  EventListView.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventListView: View {
    private let tabs = ["screenEventsFuture", "screenEventsPast"]
    @State private var selectedTab = 0
    var events: EventItemListUi
    var onEventClicked: (String) -> ()
    
    var body: some View {
        let items = selectedTab == 0 ? events.future : events.past
        ScrollView {
            LazyVStack(alignment: .leading) {
                Picker("Events:", selection: $selectedTab) {
                    ForEach(tabs.indices, id: \.self) { index in
                        Text(LocalizedStringKey(tabs[index]).stringValue()).tag(index)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
                ForEach(items, id: \.id) { event in
                    Button {
                        onEventClicked(event.id)
                    } label: {
                        EventItemView(item: event)
                    }
                }
            }
            .padding([.horizontal], 16)
        }
    }
}

struct EventListView_Previews: PreviewProvider {
    static var previews: some View {
        EventListView(
            events: EventItemListUi.companion.fake,
            onEventClicked: { eventId in }
        )
    }
}
