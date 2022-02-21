import SwiftUI
import shared

struct AppView: View {
    private var viewModel: AppViewModel
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
                    Label("Agenda", systemImage: "calendar")
                }
            
            NetworkingVM(userRepository: userRepository)
                .tabItem {
                    Label("Networking", systemImage: "person.2")
                }
            
            EventVM(agendaRepository: agendaRepository)
                .tabItem {
                    Label("Event", systemImage: "ticket")
                }
        }
        .onAppear {
            viewModel.fetchAgenda()
        }
	}
}
