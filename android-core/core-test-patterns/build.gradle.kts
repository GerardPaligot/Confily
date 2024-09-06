import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.test.patterns"
}

dependencies {
    @OptIn(ExperimentalComposeLibrary::class)
    api(compose.uiTestJUnit4)
}
