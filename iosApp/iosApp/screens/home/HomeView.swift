import SwiftUI
import shared

struct HomeView: View {
    @ObservedObject var viewModel: HomeViewModel
    private let agendaRepository: AgendaRepository
    private let userRepository: UserRepository
    private let onDisconnectedClicked: () -> ()
    
    init(agendaRepository: AgendaRepository, userRepository: UserRepository, onDisconnectedClicked: @escaping () -> ()) {
        self.agendaRepository = agendaRepository
        self.userRepository = userRepository
        self.onDisconnectedClicked = onDisconnectedClicked
        self.viewModel = HomeViewModel(repository: agendaRepository)
    }

	var body: some View {
        TabView {
            AgendaVM(agendaRepository: agendaRepository)
                .tabItem {
                    Label("screenAgenda", systemImage: "calendar")
                }

            NetworkingVM(userRepository: userRepository)
                .tabItem {
                    Label("screenNetworking", systemImage: "person.2")
                }

            PartnersVM(agendaRepository: agendaRepository)
                .tabItem {
                    Label("screenPartners", systemImage: "hands.clap")
                }

            EventVM(agendaRepository: agendaRepository, onDisconnectedClicked: self.onDisconnectedClicked)
                .tabItem {
                    Label("screenEvent", systemImage: "ticket")
                }
        }
        .onAppear {
            viewModel.fetchAgenda()
        }
        .onDisappear {
            viewModel.stop()
        }
	}
}
