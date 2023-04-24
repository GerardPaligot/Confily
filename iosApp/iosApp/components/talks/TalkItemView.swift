//
//  TalkItemView.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TalkItemView: View {
    var talk: TalkItemUi
    var titleColor: Color = Color.c4hOnBackground
    var titleTextStyle: Font = Font.title3
    var onFavoriteClicked: (_: TalkItemUi) -> ()
    
    var body: some View {
        let speakers = talk.speakers.isEmpty ? "" : Bundle.main.translation(key: "semanticTalkItemSpeakers", arguments: talk.speakers.joined(separator: ", "))
        let room = Bundle.main.translation(key: "semanticTalkItemRoom", arguments: talk.room)
        let time = Bundle.main.translation(key: "semanticTalkItemTime", arguments: talk.timeInMinutes)
        let category = Bundle.main.translation(key: "semanticTalkItemCategory", arguments: talk.category.name)
        let level = talk.level == nil ? "" : Bundle.main.translation(key: "semanticTalkItemLevel", arguments: talk.level!.toLocalized()?.stringValue() ?? "")
        VStack(alignment: .leading) {
            if (talk.category.color != nil || talk.level != nil) {
                HStack(spacing: 8) {
                    if (talk.category.color != nil) {
                        DecorativeTagView(category: talk.category)
                            .labelStyle(.titleOnly)
                    }
                    if (talk.level != nil) {
                        LevelTagView(level: talk.level!)
                    }
                }
            }
            Text(talk.title)
                .foregroundColor(titleColor)
                .font(titleTextStyle)
                .frame(maxWidth: .infinity, alignment: .leading)
            HStack {
                TagUnStyledView(
                    text: talk.room,
                    icon: "video"
                )
                TagUnStyledView(
                    text: "\(talk.timeInMinutes) minutes",
                    icon: talk.timeInMinutes <= 30 ? "bolt.badge.clock" : "clock"
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(talk.title) \(speakers) \(room) \(time) \(category) \(level)")
    }
}

struct TalkItemView_Previews: PreviewProvider {
    static var previews: some View {
        TalkItemView(
            talk: TalkItemUi.companion.fake,
            onFavoriteClicked: { TalkItemUi in }
        )
        .padding()
    }
}
