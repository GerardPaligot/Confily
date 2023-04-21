//
//  LevelTagView.swift
//  iosApp
//
//  Created by GERARD on 13/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct LevelTagView: View {
    var level: String

    var body: some View {
        TagView(
            text: level.toLocalized()?.stringValue() ?? level,
            containerColor: Color.decorativeGravel,
            contentColor: Color.decorativeOnGravel
        )
    }
}

extension LocalizedStringKey {
    var stringKey: String {
        Mirror(reflecting: self).children.first(where: { $0.label == "key" })?.value as! String
    }
}

extension String {
    static func localizedString(for key: String, locale: Locale = .current) -> String {
        let language = locale.languageCode
        let path = Bundle.main.path(forResource: language, ofType: "lproj")!
        let bundle = Bundle(path: path)!
        let localizedString = NSLocalizedString(key, bundle: bundle, comment: "")
        return localizedString
    }
    
    func toLocalized() -> LocalizedStringKey? {
        switch (self) {
        case "advanced": return LocalizedStringKey("textLevelAdvanced")
        case "intermediate": return LocalizedStringKey("textLevelIntermediate")
        case "beginner": return LocalizedStringKey("textLevelBeginner")
        default: return nil
        }
    }
}

extension LocalizedStringKey {
    func stringValue(locale: Locale = .current) -> String {
        return .localizedString(for: self.stringKey, locale: locale)
    }
}

struct LevelTagView_Previews: PreviewProvider {
    static var previews: some View {
        LevelTagView(
            level: "advanced"
        )
    }
}
