plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.games.tetris.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.games.tetris.tetrisUi)
            }
        }
    }
}
