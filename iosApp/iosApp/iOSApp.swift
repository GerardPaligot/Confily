import SwiftUI
import shared
import Firebase
import SDWebImage
import SDWebImageSVGCoder

@main
struct iOSApp: App {
    let baseUrl = Configuration.baseURL.absoluteString
    let db = DatabaseWrapper().createDb()
    
    init() {
        if (isInDebugMode == false) {
            FirebaseApp.configure()
        }
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
    }

	var body: some Scene {
        let platform = Platform(context: IOSContext())
        let api = ConferenceApi.companion.create(platform: platform, baseUrl: self.baseUrl, enableNetworkLogs: isInDebugMode)
        let settings = AppleSettings(delegate: UserDefaults.standard)
        let agendaRepository = AgendaRepositoryImpl(
            api: api,
            scheduleDao: ScheduleDao(db: db, settings: settings),
            speakerDao: SpeakerDao(db: db),
            talkDao: TalkDao(db: db),
            eventDao: EventDao(db: db, settings: settings),
            partnerDao: PartnerDao(db: db),
            featuresDao: FeaturesActivatedDao(db: db),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
        let userRepository = UserRepositoryImpl(
            userDao: UserDao(db: db, platform: platform),
            eventDao: EventDao(db: db, settings: settings),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
        let eventRepository = EventRepositoryImpl(
            api: api,
            eventDao: EventDao(db: db, settings: settings)
        )
		WindowGroup {
            AppView(agendaRepository: agendaRepository, userRepository: userRepository, eventRepository: eventRepository)
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
