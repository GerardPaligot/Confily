//
//  NetworkingViewModel.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

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
    
    func fetchEmailQrCode() {
        repository.fetchProfile { userProfileUi, error in
            self.uiState = .success(
                NetworkingUi(
                    email: (userProfileUi?.email ?? "") as String,
                    hasQrCode: userProfileUi?.qrCode != nil,
                    showQrCode: false,
                    emails: [],
                    qrcode: userProfileUi?.qrCode
                )
            )
        }
    }
    
    func fetchNewEmailQrCode(email: String) {
        if case .success(let networkingUi) = uiState {
            if (email == "") { return }
            repository.fetchProfile(email: email, firstName: "", lastName: "", company: "") { userProfileUi, error in
                if (userProfileUi != nil) {
                    self.uiState = .success(networkingUi.doCopy(
                        email: email,
                        hasQrCode: true,
                        showQrCode: false,
                        emails: networkingUi.emails,
                        qrcode: userProfileUi!.qrCode
                    ))
                } else {
                    self.uiState = .failure(error!)
                }
            }
        }
    }
    
    func displayQrCode() {
        if case .success(let userProfileUi) = uiState {
            if (userProfileUi.qrcode == nil) { return }
            self.uiState = .success(userProfileUi.doCopy(
                email: userProfileUi.email,
                hasQrCode: true,
                showQrCode: true,
                emails: userProfileUi.emails,
                qrcode: userProfileUi.qrcode)
            )
        }
    }
    
    func fetchNetworking() {
        repository.startCollectNetworking(success: { emails in
            if case .success(let userProfileUi) = self.uiState {
                self.uiState = .success(userProfileUi.doCopy(
                    email: userProfileUi.email,
                    hasQrCode: userProfileUi.hasQrCode,
                    showQrCode: userProfileUi.showQrCode,
                    emails: emails,
                    qrcode: userProfileUi.qrcode)
                )
            }
        })
    }
    
    func stop() {
        repository.stopCollectNetworking()
    }
}
