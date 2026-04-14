//
//  EventViewModel.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum EventUiState {
    case loading
    case success(EventUi)
    case failure
}

@MainActor
class EventViewModel: ObservableObject {
    private let interactor: EventInteractor = InteractorHelper().eventInteractor
    private let mapInteractor: MapInteractor = InteractorHelper().mapInteractor

    @Published var uiState: EventUiState = EventUiState.loading
    @Published var hasMaps: Bool = false
    @Published var hasMenus: Bool = false

    private var eventTask: Task<(), Never>?
    private var mapsTask: Task<(), Never>?
    private var menusTask: Task<(), Never>?

    func fetchEvent() {
        eventTask = Task {
            do {
                let stream = asyncSequence(for: interactor.event())
                for try await event in stream {
                    if (event != nil) {
                        self.uiState = .success(event!)
                    } else {
                        print("Nullpointer")
                    }
                }
            } catch {
                print(error)
                self.uiState = .failure
            }
        }
        mapsTask = Task {
            do {
                let stream = asyncSequence(for: mapInteractor.mapsFilled())
                for try await maps in stream {
                    self.hasMaps = !maps.isEmpty
                }
            } catch {
                self.hasMaps = false
            }
        }
        menusTask = Task {
            do {
                let stream = asyncSequence(for: interactor.menus())
                for try await menus in stream {
                    self.hasMenus = !menus.isEmpty
                }
            } catch {
                self.hasMenus = false
            }
        }
    }

    func stop() {
        eventTask?.cancel()
        mapsTask?.cancel()
        menusTask?.cancel()
    }

    func saveTicket(barcode: String) async {
        if (await asyncError(for: interactor.insertOrUpdateTicket(barcode: barcode))) != nil {
            // ignore
        }
    }
}
