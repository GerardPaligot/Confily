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
import AsyncAlgorithms
import KMPNativeCoroutinesAsync

enum UserProfileUiState {
    case loading
    case success(NetworkingUi)
    case failure(Error)
}

@MainActor
class NetworkingViewModel: ObservableObject {
    let repository: UserRepository

    init(repository: UserRepository) {
        self.repository = repository
    }
    
    @Published var uiState: UserProfileUiState = UserProfileUiState.loading
    
    private var networkingTask: Task<(), Never>?
    
    func fetchNetworking() {
        networkingTask = Task {
            do {
                let networkingStream = asyncStream(for: repository.fetchNetworkingNative())
                let profileStream = asyncStream(for: repository.fetchProfileNative())
                for try await (networkings, profile) in combineLatest(networkingStream, profileStream) {
                    let profileUi = profile != nil ? profile! : UserProfileUi(email: "", firstName: "", lastName: "", company: "", qrCode: nil)
                    self.uiState = .success(NetworkingUi(userProfileUi: profileUi, showQrCode: false, users: networkings))
                }
            } catch {
                self.uiState = .failure(error)
            }
        }
    }
    
    func stop() {
        networkingTask?.cancel()
    }
    
    func saveProfile(email: String, firstName: String, lastName: String, company: String) {
        if (email == "") { return }
        repository.saveProfile(email: email, firstName: firstName, lastName: lastName, company: company)
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
                let inserted = repository.insertNetworkingProfile(user: user)
                callback(inserted)
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
        repository.deleteNetworkProfile(email: email)
    }
}
