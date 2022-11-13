//
//  MenusViewModel.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

enum MenusUiState {
    case loading
    case success(Array<MenuItemUi>)
    case failure
}

@MainActor
class MenusViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: MenusUiState = MenusUiState.loading
    
    func fetchMenus() {
        repository.startCollectMenus(
            success: { menus in
                self.uiState = MenusUiState.success(menus)
            },
            failure: { throwable in
                self.uiState = MenusUiState.failure
            }
        )
    }
    
    func stop() {
        repository.stopCollectMenus()
    }
}
