import SwiftUI
import SharedDi
import Firebase
import NSExceptionKtCrashlytics
import SDWebImage
import SDWebImageSVGCoder

var isInDebugMode: Bool {
    #if DEBUG
        return true
    #else
        return false
    #endif
}

@main
struct iOSApp: App {
    let viewModelFactory: ViewModelFactory

    init() {
        if (isInDebugMode == false) {
            FirebaseApp.configure()
            NSExceptionKt.addReporter(.crashlytics(causedByStrategy: .append))
        }
        HelperKt.doInitKoin()
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
        self.viewModelFactory = ViewModelFactory()
    }

	var body: some Scene {
        WindowGroup {
            AppView(
                viewModel: self.viewModelFactory.makeAppViewModel()
            )
            .environmentObject(viewModelFactory)
        }
	}
}
