//
//  DecorativeTagView.swift
//  iosApp
//
//  Created by GERARD on 13/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DecorativeTagView: View {
    var category: CategoryUi
    
    var body: some View {
        switch category.color {
        case "amethyst":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeAmethyst,
                contentColor: Color.decorativeOnAmethyst
            )
        
        case "cobalt":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeCobalt,
                contentColor: Color.decorativeOnCobalt
            )
            
        case "brick":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeBrick,
                contentColor: Color.decorativeOnBrick
            )
            
        case "emerald":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeEmerald,
                contentColor: Color.decorativeOnEmerald
            )
            
        case "jade":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeJade,
                contentColor: Color.decorativeOnJade
            )
            
        case "saffron":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeSaffron,
                contentColor: Color.decorativeOnSaffron
            )
            
        case "gold":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeGold,
                contentColor: Color.decorativeOnGold
            )
            
        case "gravel":
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeGravel,
                contentColor: Color.decorativeOnGravel
            )
            
        default:
            TagView(
                text: category.name,
                icon: category.icon?.iconSFSymbol(),
                containerColor: Color.decorativeEmerald,
                contentColor: Color.decorativeOnEmerald
            )
        }
    }
}

private extension String {
    func iconSFSymbol() -> String? {
        switch (self) {
        case "database": return "server.rack"
        case "computer": return "desktopcomputer"
        case "public": return "globe.europe.africa"
        case "cloud": return "cloud"
        case "smartphone": return "iphone"
        case "lock": return "lock"
        case "autorenew": return "arrow.clockwise"
        case "psychology": return "brain.head.profile"
        case "draw": return "pencil.line"
        case "language": return "globe"
        default: return nil
        }
    }
}

struct DecorativeTagView_Previews: PreviewProvider {
    static var previews: some View {
        DecorativeTagView(
            category: TalkItemUi.companion.fake.category
        )
    }
}
