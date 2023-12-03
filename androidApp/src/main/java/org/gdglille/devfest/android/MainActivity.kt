package org.gdglille.devfest.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.res.stringResource
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.Main
import org.gdglille.devfest.android.theme.m3.style.R
import java.io.File

@Suppress("LongMethod")
@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val openfeedbackFirebaseConfig = (application as MainApplication).openFeedbackConfig

        val workManager = WorkManager.getInstance(this)
        setContent {
            val inDarkTheme = isSystemInDarkTheme()
            DisposableEffect(inDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { inDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(lightScrim, darkScrim) { inDarkTheme },
                )
                onDispose {}
            }
            navController = rememberNavController()
            val exportSubject = stringResource(id = R.string.text_export_subject)
            val reportSubject = stringResource(id = R.string.text_report_subject)
            val reportAppTarget = stringResource(id = R.string.text_report_app_target)
            Main(
                openfeedbackFirebaseConfig = openfeedbackFirebaseConfig,
                launchUrl = { launchUrl(it) },
                onContactExportClicked = { export ->
                    val uri: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "${applicationContext.packageName}.provider",
                        File(export.filePath)
                    )
                    val intent = ShareCompat.IntentBuilder(this)
                        .setStream(uri)
                        .intent
                        .setAction(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:${export.mailto ?: ""}"))
                        .putExtra(Intent.EXTRA_SUBJECT, exportSubject)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                },
                onReportByPhoneClicked = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$it")
                    }
                    startActivity(intent)
                },
                onReportByEmailClicked = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$it")
                        putExtra(Intent.EXTRA_SUBJECT, reportSubject)
                    }
                    startActivity(Intent.createChooser(intent, reportAppTarget))
                },
                onShareClicked = { text ->
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                },
                onItineraryClicked = { lat, lng ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("google.navigation:q=$lat,$lng")
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    startActivity(shareIntent)
                },
                onScheduleStarted = {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val request = OneTimeWorkRequestBuilder<ScheduleWorkManager>()
                        .setConstraints(constraints)
                        .build()
                    workManager.enqueue(request)
                },
                navController = navController
            )
        }
    }

    companion object {
        /**
         * The default light scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

        /**
         * The default dark scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

        fun create(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
