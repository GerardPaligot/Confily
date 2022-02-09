import SwiftUI
import shared

struct ContentView: View {
    var agendaRepository: AgendaRepository

	var body: some View {
        NavigationView {
            AgendaVM(
                viewModel: AgendaViewModel(repository: agendaRepository),
                onTalkClicked: { String in }
            )
                .navigationBarTitle(Text("Agenda"))
        }
	}
}
