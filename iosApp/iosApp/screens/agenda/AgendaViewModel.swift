//
//  AgendaViewModel.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

enum AgendaUiState {
    case loading
    case success(AgendaUi)
    case failure
}

class AgendaViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    
    func fetchAgenda() {
        repository.startCollectAgenda(
            success: { agenda in
                self.uiState = AgendaUiState.success(agenda)
            },
            failure: { throwable in
                self.uiState = AgendaUiState.failure
            }
        )
    }
    
    func stop() {
        repository.stopCollectAgenda()
    }
    
    func markAsFavorite(talkItem: TalkItemUi) {
        let scheduleId = talkItem.id
        let isFavorite = !talkItem.isFavorite
        repository.markAsRead(scheduleId: scheduleId, isFavorite: isFavorite, completionHandler: {_,_ in
        })
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
