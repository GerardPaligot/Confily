import SwiftUI

struct HomeView: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @ObservedObject var viewModel: HomeViewModel
    private let onDisconnectedClicked: () -> ()
    
    init(viewModel: HomeViewModel, onDisconnectedClicked: @escaping () -> ()) {
        self.onDisconnectedClicked = onDisconnectedClicked
        self.viewModel = viewModel
    }

	var body: some View {
        TabView {
            AgendaVM(viewModel: viewModelFactory.makeAgendaViewModel())
                .tabItem {
                    Label("screenAgenda", systemImage: "calendar")
                }

            NetworkingVM(viewModel: viewModelFactory.makeNetworkingViewModel())
                .tabItem {
                    Label("screenNetworking", systemImage: "person.2")
                }

            PartnersVM(viewModel: viewModelFactory.makePartnersViewModel())
                .tabItem {
                    Label("screenPartners", systemImage: "hands.clap")
                }

            EventVM(viewModel: self.viewModelFactory.makeEventViewModel(),onDisconnectedClicked: self.onDisconnectedClicked)
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
