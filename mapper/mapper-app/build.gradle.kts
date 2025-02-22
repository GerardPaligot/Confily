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
    packageName = "com.paligot.confily.mapper"

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

android {
    namespace = "com.paligot.confily.map"
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.shared.coreDi)
            implementation(projects.style.theme)
            implementation(projects.mapper.mapperFeatures.mapList.mapListDi)
            implementation(projects.mapper.mapperFeatures.mapList.mapListPresentation)
            implementation(projects.mapper.mapperFeatures.mapList.mapListRoutes)
            implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailDi)
            implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailPresentation)
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
        mainClass = "com.paligot.confily.map.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.paligot.confily.map"
            packageVersion = "1.0.0"
        }
    }
}
