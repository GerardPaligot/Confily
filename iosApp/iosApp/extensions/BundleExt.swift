//
//  BundleExt.swift
//  iosApp
//
//  Created by GERARD on 21/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension Bundle {
    func translation(key: String, arguments: CVarArg...) -> String {
        let format = localizedString(forKey: key, value: "", table: nil)
        return String.localizedStringWithFormat(format, arguments)
    }
}
