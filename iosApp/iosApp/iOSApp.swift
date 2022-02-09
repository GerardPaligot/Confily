import SwiftUI
import shared

@main
struct iOSApp: App {
    let baseUrl = "https://cms4partners-ce427.nw.r.appspot.com"
    let eventId = "AFtTtSMq1NY4tnK2gMg1"
    let db = DatabaseWrapper().createDb()

	var body: some Scene {
        let api = ConferenceApi.companion.create(baseUrl: self.baseUrl, eventId: self.eventId, enableNetworkLogs: true)
        let agendaRepository = AgendaRepositoryImpl(
            api: api,
            scheduleDao: ScheduleDao(db: db, eventId: eventId),
            speakerDao: SpeakerDao(db: db),
            talkDao: TalkDao(db: db),
            eventDao: EventDao(db: db, eventId: eventId)
        )
		WindowGroup {
			ContentView(agendaRepository: agendaRepository)
		}
	}
}
