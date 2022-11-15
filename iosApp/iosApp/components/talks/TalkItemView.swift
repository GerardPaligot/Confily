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
    var roomColor: Color = Color.c4hOnBackground
    var roomTextStyle: Font = Font.callout
    var subtitleColor: Color = Color.c4hOnBackground
    var subtitleTextStyle: Font = Font.callout
    var onFavoriteClicked: (_: TalkItemUi) -> ()
    
    var body: some View {
        ZStack {
            VStack(alignment: .leading) {
                if (talk.category.color != nil || talk.level != nil) {
                    HStack(spacing: 8) {
                        if (talk.category.color != nil) {
                            DecorativeTagView(category: talk.category)
                        }
                        if (talk.level != nil) {
                            LevelTagView(level: talk.level!)
                        }
                    }
                }
                Text(talk.title)
                    .foregroundColor(titleColor)
                    .font(titleTextStyle)
                    .padding(.bottom, 4)
                if (talk.speakers.count != 0) {
                    Text(talk.speakers.joined(separator: ", "))
                        .foregroundColor(subtitleColor)
                        .font(subtitleTextStyle)
                        .opacity(0.5)
                }
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
            .accessibilityElement(children: .combine)
            .accessibilityAddTraits(!talk.isPause ? .isButton : [])
            .accessibilityRemoveTraits(!talk.isPause ? .isStaticText : [])
            if (!talk.isPause) {
                ZStack {
                    let iconName = talk.isFavorite ? "star.fill" : "star"
                    let iconColor = talk.isFavorite ? Color.c4hSecondary : Color.c4hOnBackground
                    Button {
                        onFavoriteClicked(talk)
                    } label: {
                        Image(systemName: iconName)
                            .foregroundColor(iconColor)
                            .padding()
                    }
                    .accessibilityAddTraits(talk.isFavorite ? [.isButton, .isSelected] : .isButton)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
            }
        }
    }
}

struct TalkItemView_Previews: PreviewProvider {
    static var previews: some View {
        TalkItemView(
            talk: TalkItemUi.companion.fake,
            onFavoriteClicked: { TalkItemUi in }
        )
    }
}
