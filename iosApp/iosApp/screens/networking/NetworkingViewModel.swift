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
                    self.uiState = .success(NetworkingUi(userProfileUi: profile, showQrCode: false, users: networkings))
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
    
    func saveNetworkingProfile(text: String) {
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
                repository.insertNetworkingProfile(user: user)
                if case .success(let networkingUi) = uiState {
                    self.uiState = .success(networkingUi.doCopy(
                        userProfileUi: networkingUi.userProfileUi,
                        showQrCode: false,
                        users: networkingUi.users
                    ))
                }
            } catch {
                // ignored
            }
        }
    }
    
    func showQrcode() {
        if case .success(let networkingUi) = uiState {
            self.uiState = .success(networkingUi.doCopy(
                    userProfileUi: networkingUi.userProfileUi,
                    showQrCode: true,
                    users: networkingUi.users
                )
            )
        }
    }
    
    func deleteNetworkProfile(email: String) {
        repository.deleteNetworkProfile(email: email)
    }
    
    func exportNetworking() -> ComposeMailData {
        let networking = repository.exportNetworking()
        let recipients = networking.mailto != nil ? [networking.mailto!] : []
        do {
            return ComposeMailData(
                subject: "Networking export",
                recipients: recipients,
                message: "",
                attachments: [
                    AttachmentData(
                        data: try Data(contentsOf: URL(fileURLWithPath: networking.filePath), options: Data.ReadingOptions()),
                        mimeType: "plain/text",
                        fileName: "export.csv"
                    )
                ]
            )
        } catch {
            print("Error occurred when we try to export networking \(error)")
            return ComposeMailData.empty
        }
    }
}
