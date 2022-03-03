//
//  NetworkingViewModel.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import CoreImage
import SwiftUI

enum UserProfileUiState {
    case loading
    case success(NetworkingUi)
    case failure(Error)
}

class NetworkingViewModel: ObservableObject {
    let repository: UserRepository

    init(repository: UserRepository) {
        self.repository = repository
    }
    
    @Published var uiState: UserProfileUiState = UserProfileUiState.loading
    
    func fetchProfile() {
        repository.fetchProfile { userProfileUi, error in
            self.uiState = .success(
                NetworkingUi(
                    userProfileUi: userProfileUi ?? UserProfileUi(email: "", firstName: "", lastName: "", company: "", qrCode: nil),
                    showQrCode: false,
                    emails: []
                )
            )
        }
    }
    
    func fetchProfile(email: String, firstName: String, lastName: String, company: String) {
        if case .success(let networkingUi) = uiState {
            if (email == "") { return }
            repository.saveProfile(email: email, firstName: firstName, lastName: lastName, company: company) { userProfileUi, error in
                if (userProfileUi != nil) {
                    self.uiState = .success(networkingUi.doCopy(
                        userProfileUi: userProfileUi!,
                        showQrCode: true,
                        emails: networkingUi.emails
                    ))
                } else {
                    self.uiState = .failure(error!)
                }
            }
        }
    }
    
    func displayQrCode() {
        if case .success(let networkingUi) = uiState {
            self.uiState = .success(networkingUi.doCopy(
                    userProfileUi: networkingUi.userProfileUi,
                    showQrCode: true,
                    emails: networkingUi.emails
                )
            )
        }
    }
    
    func closeQrCode() {
        if case .success(let networkingUi) = uiState {
            self.uiState = .success(networkingUi.doCopy(
                userProfileUi: networkingUi.userProfileUi,
                showQrCode: false,
                emails: networkingUi.emails
            ))
        }
    }
    
    func fetchNetworking() {
        repository.startCollectNetworking(success: { emails in
            if case .success(let networkingUi) = self.uiState {
                self.uiState = .success(networkingUi.doCopy(
                        userProfileUi: networkingUi.userProfileUi,
                        showQrCode: networkingUi.showQrCode,
                        emails: emails
                    )
                )
            }
        })
    }
    
    func stop() {
        repository.stopCollectNetworking()
    }
}
