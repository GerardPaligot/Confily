import SwiftUI
import shared

struct AppView: View {
    @ObservedObject var viewModel: AppViewModel
    private let agendaRepository: AgendaRepository
    private let userRepository: UserRepository
    
    init(agendaRepository: AgendaRepository, userRepository: UserRepository) {
        self.agendaRepository = agendaRepository
        self.userRepository = userRepository
        self.viewModel = AppViewModel(repository: agendaRepository)
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

            EventVM(agendaRepository: agendaRepository)
                .tabItem {
                    Label("screenEvent", systemImage: "ticket")
                }
        }
        .onAppear {
            Task {
                await viewModel.fetchAgenda()
            }
        }
	}
}
