import com.codingfeline.buildkonfig.compiler.FieldSpec
import extensions.toProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("confily.application")
    id("confily.quality")
    alias(libs.plugins.buildkonfig)
}

val secretsProps = rootProject.file("config/secrets.properties").toProperties()
val secretsExampleProps = rootProject.file("config/secrets.properties.example").toProperties()
val appProps = rootProject.file("config/app.properties").toProperties()
buildkonfig {
    packageName = "com.paligot.confily.map.editor"

    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "BASE_URL",
            value = appProps["BASE_URL"] as String
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "DEFAULT_EVENT",
            value = appProps["DEFAULT_EVENT"] as String
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "API_KEY",
            value = secretsProps["API_KEY"] as String? ?: secretsExampleProps["API_KEY"] as String
        )
    }
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.shared.coreDi)
            implementation(projects.style.theme)
            implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListDi)
            implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListPresentation)
            implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListRoutes)
            implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailDi)
            implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailPresentation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)
            implementation(libs.bundles.koin)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.paligot.confily.map.editor.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.paligot.confily.map.editor"
            packageVersion = "1.0.0"
        }
    }
}
