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
    var titleTextStyle: Font = Font.body
    var roomColor: Color = Color.c4hOnBackground
    var roomTextStyle: Font = Font.callout
    var subtitleColor: Color = Color.c4hOnBackground
    var subtitleTextStyle: Font = Font.callout
    var onFavoriteClicked: (_: String, _: Bool) -> ()
    
    var body: some View {
        HStack(alignment: .center) {
            VStack(alignment: .leading) {
                Text(talk.title)
                    .foregroundColor(titleColor)
                    .font(titleTextStyle)
                Text(talk.room)
                    .foregroundColor(roomColor)
                    .font(roomTextStyle)
                    .opacity(0.5)
                Text(talk.speakers.joined(separator: ", "))
                    .foregroundColor(subtitleColor)
                    .font(subtitleTextStyle)
                    .opacity(0.5)
            }
            .frame(maxWidth: .infinity, alignment: .topLeading)
            let iconName = talk.isFavorite ? "star.fill" : "star"
            let iconColor = talk.isFavorite ? Color.c4hSecondary : Color.c4hOnBackground
            Button {
                onFavoriteClicked(talk.id, !talk.isFavorite)
            } label: {
                Image(systemName: iconName)
                    .foregroundColor(iconColor)
                    .padding()
            }
        }
    }
}

struct TalkItemView_Previews: PreviewProvider {
    static var previews: some View {
        TalkItemView(
            talk: TalkItemUi.companion.fake,
            onFavoriteClicked: { String, Bool in }
        )
    }
}
