import SwiftUI
import shared
import Firebase
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
        }
        SDImageCodersManager.shared.addCoder(SDImageSVGCoder.shared)
        self.viewModelFactory = ViewModelFactory(isDebug: isInDebugMode)
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
