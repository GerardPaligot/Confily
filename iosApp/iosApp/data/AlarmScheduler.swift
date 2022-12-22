//
//  AlarmScheduler.swift
//  iosApp
//
//  Created by GERARD on 22/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

class AlarmScheduler {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }
    
    func schedule(talkItem: TalkItemUi) async {
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
