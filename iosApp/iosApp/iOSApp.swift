import SwiftUI
import shared
import Firebase
import SDWebImage
import SDWebImageSVGCoder

@main
struct iOSApp: App {
    let baseUrl = "https://cms4partners-ce427.nw.r.appspot.com"
    let eventId = "2022"
    let db = DatabaseWrapper().createDb()
    
    init() {
        FirebaseApp.configure()
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
    }

	var body: some Scene {
        let api = ConferenceApi.companion.create(baseUrl: self.baseUrl, eventId: self.eventId, enableNetworkLogs: isInDebugMode)
        let settings = AppleSettings(delegate: UserDefaults.standard)
        let agendaRepository = AgendaRepositoryImpl(
            api: api,
            scheduleDao: ScheduleDao(db: db, settings: settings, eventId: eventId),
            speakerDao: SpeakerDao(db: db, eventId: eventId),
            talkDao: TalkDao(db: db),
            eventDao: EventDao(db: db, eventId: eventId),
            userDao: UserDao(db: db, eventId: eventId),
            featuresDao: FeaturesActivatedDao(db: db, eventId: eventId),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
        let userRepository = UserRepositoryImpl(
            userDao: UserDao(db: db, eventId: eventId),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
		WindowGroup {
			AppView(agendaRepository: agendaRepository, userRepository: userRepository)
		}
	}
    
    var isInDebugMode: Bool {
        #if DEBUG
            return true
        #else
            return false
        #endif
    }
}
