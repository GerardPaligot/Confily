//
//  PauseView.swift
//  iosApp
//
//  Created by GERARD on 15/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PauseView: View {
    var body: some View {
        ZStack {
            Text("textPauseItem")
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.decorativeBrick)
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}

struct PauseView_Previews: PreviewProvider {
    static var previews: some View {
        PauseView()
    }
}
