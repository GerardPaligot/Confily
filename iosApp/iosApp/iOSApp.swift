import SwiftUI
import shared
import Firebase
import SDWebImage
import SDWebImageSVGCoder

@main
struct iOSApp: App {
    let baseUrl = "https://cms4partners-ce427.nw.r.appspot.com"
    let db = DatabaseWrapper().createDb()
    
    init() {
        if (isInDebugMode == false) {
            FirebaseApp.configure()
        }
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
    }

	var body: some Scene {
        let api = ConferenceApi.companion.create(baseUrl: self.baseUrl, enableNetworkLogs: isInDebugMode)
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
            userDao: UserDao(db: db),
            eventDao: EventDao(db: db, settings: settings),
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
