import SwiftUI
import shared
import SDWebImage
import SDWebImageSVGCoder

@main
struct iOSApp: App {
    let baseUrl = "https://cms4partners-ce427.nw.r.appspot.com"
    let eventId = "2022"
    let db = DatabaseWrapper().createDb()
    
    init() {
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
    }

	var body: some Scene {
        let api = ConferenceApi.companion.create(baseUrl: self.baseUrl, eventId: self.eventId, enableNetworkLogs: true)
        let agendaRepository = AgendaRepositoryImpl(
            api: api,
            scheduleDao: ScheduleDao(db: db, eventId: eventId),
            speakerDao: SpeakerDao(db: db),
            talkDao: TalkDao(db: db),
            eventDao: EventDao(db: db, eventId: eventId)
        )
        let userRepository = UserRepositoryImpl(
            userDao: UserDao(db: db, settings: AppleSettings(delegate: UserDefaults.standard), eventId: eventId),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
		WindowGroup {
			AppView(agendaRepository: agendaRepository, userRepository: userRepository)
		}
	}
}
