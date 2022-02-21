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
    case success(UserProfileUi)
    case failure(Error)
}

class UserProfileViewModel: ObservableObject {
    let repository: UserRepository

    init(repository: UserRepository) {
        self.repository = repository
    }
    
    @Published var uiState: UserProfileUiState = UserProfileUiState.loading
    
    func fetchEmailQrCode() {
        repository.fetchEmailQrCode { image, error in
            self.uiState = .success(
                UserProfileUi(
                    email: (image?.first ?? "") as String,
                    hasQrCode: image?.second != nil,
                    showQrCode: false,
                    emails: [],
                    qrcode: image?.second
                )
            )
        }
    }
    
    func fetchNewEmailQrCode(email: String) {
        if case .success(let userProfileUi) = uiState {
            if (email == "") { return }
            repository.fetchEmailQrCode(email: email) { image, error in
                if (image != nil) {
                    self.uiState = .success(userProfileUi.doCopy(
                        email: email,
                        hasQrCode: true,
                        showQrCode: false,
                        emails: userProfileUi.emails,
                        qrcode: image
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
