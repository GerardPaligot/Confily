//
//  UIApplicationExt.swift
//  iosApp
//
//  Created by GERARD on 20/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit

extension UIApplication {
    func endEditing() {
        sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
