//
//  MenusVM.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MenusVM: View {
    @ObservedObject var viewModel: MenusViewModel

    init(viewModel: MenusViewModel) {
        self.viewModel = viewModel
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let menus):
                    Menus(menuItems: menus)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchMenus()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
