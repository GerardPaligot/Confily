import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import extensions.toProperties

plugins {
    id("confily.sample")
    id("confily.quality")
    alias(libs.plugins.buildkonfig)
}

android {
    namespace = "com.paligot.confily.schedules.sample"

    dependencies {
        debugImplementation(libs.androidx.compose.ui.test.manifest)
        constraints {
            implementation("androidx.tracing:tracing:1.2.0") {
                because("AndroidX Test gets force-downgraded to 1.0.0 and breaks otherwise")
            }
        }
    }
}

val appProps = rootProject.file("config/app.properties").toProperties()
buildkonfig {
    packageName = "com.paligot.confily.schedules.sample"

    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", appProps["BASE_URL"] as String)
        buildConfigField(STRING, "DEFAULT_EVENT", appProps["DEFAULT_EVENT"] as String)
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.schedules.schedulesPresentation)
            implementation(projects.features.schedules.schedulesDi)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.core.coreModelsFactory)
            implementation(projects.core.coreTestPatterns)
            implementation(projects.core.coreTest)
            implementation(projects.features.schedules.schedulesTest)
            implementation(compose.desktop.uiTestJUnit4)
            implementation(libs.settings.test)
        }
    }
}
