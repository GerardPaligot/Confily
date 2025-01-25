//
//  VideoPlayerUIView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 25/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import AVKit

struct VideoPlayerUIView: View {
    let player: AVPlayer
    
    init(url: String) {
        self.player = AVPlayer(url: URL(string: url)!)
    }
    
    var body: some View {
        VideoPlayer(player: self.player)
            .edgesIgnoringSafeArea(.all)
            .onAppear {
                player.play()
            }
            .onDisappear {
                player.pause()
            }
    }
}

#Preview {
    VideoPlayerUIView(
        url: "https://storage.googleapis.com/confily/droidcon-london/partners/FYSgCVcsM1ZJ2R44Y7uy/bitrise.mp4"
    )
}
