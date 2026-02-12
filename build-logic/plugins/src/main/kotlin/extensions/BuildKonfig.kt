package extensions

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Project

fun BuildKonfigExtension.configBuildKonfig(project: Project) {
    packageName = "com.paligot.confily"

    defaultConfigs {
        buildConfigField(STRING, "APP_ID", project.appProps.appId)
        buildConfigField(STRING, "BASE_URL", project.appProps["BASE_URL"] as String)
        buildConfigField(STRING, "DEFAULT_EVENT", project.appProps["DEFAULT_EVENT"] as String)
        buildConfigField(STRING, "DEFAULT_LANGUAGE", project.appProps["DEFAULT_LANGUAGE"] as String)
        buildConfigField(STRING, "FIREBASE_PROJECT_ID", project.appProps["FIREBASE_PROJECT_ID"] as String)
        buildConfigField(STRING, "FIREBASE_APP_ID", project.appProps["FIREBASE_APP_ID"] as String)
        buildConfigField(STRING, "FIREBASE_API_KEY", project.appProps["FIREBASE_API_KEY"] as String)
        buildConfigField(STRING, "VERSION_CODE", project.versionProps.versionName)
    }
}
