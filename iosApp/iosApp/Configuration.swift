//
//  Configuration.swift
//  iosApp
//
//  Created by GERARD on 11/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

enum Configuration {
    static var baseURL: URL {
        URL(string: string(for: "BASE_URL"))!
    }

    static private func string(for key: String) -> String {
        Bundle.main.infoDictionary?[key] as! String
    }
}
