import SwiftUI
import shared

struct AppView: View {
    private var viewModel: AppViewModel
    private let agendaRepository: AgendaRepository
    
    init(agendaRepository: AgendaRepository) {
        self.agendaRepository = agendaRepository
        self.viewModel = AppViewModel(repository: agendaRepository)
    }

	var body: some View {
        TabView {
            AgendaVM(agendaRepository: agendaRepository)
                .tabItem {
                    Label("Agenda", systemImage: "calendar")
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
