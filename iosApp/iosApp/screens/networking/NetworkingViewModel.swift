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
import Contacts

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
                    users: []
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
                        users: networkingUi.users
                    ))
                } else {
                    self.uiState = .failure(error!)
                }
            }
        }
    }
    
    func saveNetworkingProfile(text: String, callback: @escaping (Bool) -> ()) {
        if let data = text.data(using: .unicode) {
            do {
                let contacts = try CNContactVCardSerialization.contacts(with: data)
                let contact = contacts.first
                let user = UserNetworkingUi(
                    email: (contact?.emailAddresses.first?.value ?? "") as String,
                    firstName: contact?.givenName ?? "",
                    lastName: contact?.familyName ?? "",
                    company: contact?.organizationName ?? ""
                )
                repository.insertNetworkingProfile(user: user) { response, _ in
                    if (response != nil) {
                        callback(response! as! Bool)
                    } else {
                        callback(false)
                    }
                }
            } catch {
                // ignored
            }
        }
    }
    
    func displayQrCode() {
        if case .success(let networkingUi) = uiState {
            self.uiState = .success(networkingUi.doCopy(
                    userProfileUi: networkingUi.userProfileUi,
                    showQrCode: true,
                    users: networkingUi.users
                )
            )
        }
    }
    
    func closeQrCode() {
        if case .success(let networkingUi) = uiState {
            self.uiState = .success(networkingUi.doCopy(
                userProfileUi: networkingUi.userProfileUi,
                showQrCode: false,
                users: networkingUi.users
            ))
        }
    }
    
    func deleteNetworkProfile(email: String) {
        repository.deleteNetworkProfile(email: email) { _, error in }
    }
    
    func fetchNetworking() {
        repository.startCollectNetworking(success: { users in
            if case .success(let networkingUi) = self.uiState {
                self.uiState = .success(networkingUi.doCopy(
                        userProfileUi: networkingUi.userProfileUi,
                        showQrCode: networkingUi.showQrCode,
                        users: users
                    )
                )
            }
        })
    }
    
    func stop() {
        repository.stopCollectNetworking()
    }
}
