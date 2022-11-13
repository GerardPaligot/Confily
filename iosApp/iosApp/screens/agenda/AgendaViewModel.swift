//
//  AgendaViewModel.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

enum AgendaUiState {
    case loading
    case success(AgendaUi)
    case failure
}

@MainActor
class AgendaViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    
    private var agendaTask: Task<(), Never>?
    
    func toggleFavoriteFiltering() {
        repository.toggleFavoriteFiltering()
    }

    func fetchAgenda() {
        agendaTask = Task {
            do {
                let stream = asyncStream(for: repository.agendaNative(date: "2022-06-10"))
                for try await agenda in stream {
                    self.uiState = AgendaUiState.success(agenda)
                }
            } catch {
                self.uiState = AgendaUiState.failure
            }
        }
    }
    
    func stop() {
        agendaTask?.cancel()
    }
    
    func markAsFavorite(talkItem: TalkItemUi) async {
        let scheduleId = talkItem.id
        let isFavorite = !talkItem.isFavorite
        repository.markAsRead(scheduleId: scheduleId, isFavorite: isFavorite)
        if (isFavorite) {
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { success, error in
                if (success) {
                    let content = UNMutableNotificationContent()
                    content.title = "Talk dans 10 minutes en \(talkItem.room.lowercased())"
                    content.subtitle = talkItem.speakers.joined(separator: ", ")
                    content.body = talkItem.title
                    
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
                    dateFormatter.timeZone = TimeZone.current
                    dateFormatter.locale = Locale.current
                    let date = dateFormatter.date(from: talkItem.startTime)
                    let modifiedDate = Calendar.current.date(byAdding: .second, value: -50, to: date!)
                    let interval = modifiedDate?.timeIntervalSinceNow ?? 0
                    if (interval < 0) {
                        return
                    }

                    let trigger = UNTimeIntervalNotificationTrigger(timeInterval: interval, repeats: false)
                    
                    let request = UNNotificationRequest(identifier: scheduleId, content: content, trigger: trigger)
                    UNUserNotificationCenter.current().add(request) { (error) in }
                }
            }
        } else {
            UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [scheduleId])
        }
    }
}
