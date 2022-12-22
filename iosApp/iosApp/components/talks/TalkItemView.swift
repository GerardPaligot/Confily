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
        VStack(alignment: .leading) {
            HStack(alignment: .top) {
                Text(talk.title)
                    .foregroundColor(titleColor)
                    .font(titleTextStyle)
                    .frame(maxWidth: .infinity, alignment: .leading)
                if (!talk.isPause) {
                    let iconName = talk.isFavorite ? "star.fill" : "star"
                    let iconColor = talk.isFavorite ? Color.c4hSecondary : Color.c4hOnBackground
                    Button {
                        onFavoriteClicked(talk)
                    } label: {
                        Image(systemName: iconName)
                            .foregroundColor(iconColor)
                            .padding(8)
                    }
                    .accessibilityAddTraits(
                        talk.isFavorite ? [.isButton, .isSelected] : .isButton
                    )
                }
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
            Divider()
            ZStack(alignment: .leading) {
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
                if (talk.speakers.count != 0) {
                    SpeakersAvatarView(
                        speakers: talk.speakers,
                        avatars: talk.speakersAvatar
                    )
                    .frame(maxWidth: .infinity, alignment: .trailing)
                }
            }
            .padding([.top], 8)
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .padding()
        .accessibilityElement(children: .combine)
        .accessibilityAddTraits(!talk.isPause ? .isButton : [])
        .accessibilityRemoveTraits(!talk.isPause ? .isStaticText : [])
        .background(Color.c4hSurface)
        .modifier(CardModifier(elevation: 2))
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
