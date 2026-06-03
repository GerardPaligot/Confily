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

            if viewModel.hasNetworking {
                NetworkingVM(viewModel: viewModelFactory.makeNetworkingViewModel())
                    .tabItem {
                        Label("screenNetworking", systemImage: "person.2")
                    }
            }

            PartnersVM(viewModel: viewModelFactory.makePartnersViewModel())
                .tabItem {
                    Label("screenPartners", systemImage: "hands.clap")
                }

            if viewModel.hasQuiz {
                QuizVM(viewModel: viewModelFactory.makeQuizViewModel())
                    .tabItem {
                        Label("screenQuiz", systemImage: "questionmark.circle")
                    }
            }

            EventVM(viewModel: self.viewModelFactory.makeEventViewModel(),onDisconnectedClicked: self.onDisconnectedClicked)
                .tabItem {
                    Label("screenEvent", systemImage: "ticket")
                }
        }
        .onAppear {
            viewModel.fetchAgenda()
            viewModel.fetchFeatureFlags()
        }
        .onDisappear {
            viewModel.stop()
        }
	}
}
