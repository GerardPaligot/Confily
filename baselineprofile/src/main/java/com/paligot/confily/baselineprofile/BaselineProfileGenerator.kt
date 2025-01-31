package com.paligot.confily.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.paligot.confily.baselineprofile.scenarios.AgendaPOM
import com.paligot.confily.baselineprofile.scenarios.EventListPOM
import com.paligot.confily.baselineprofile.scenarios.InfoPOM
import com.paligot.confily.baselineprofile.scenarios.NetworkingPOM
import com.paligot.confily.baselineprofile.scenarios.PartnersPOM
import com.paligot.confily.baselineprofile.scenarios.SpeakersPOM
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :composeApp:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            // See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true,
            maxIterations = 1
        ) {
            val eventListPom = EventListPOM(device)
            val agendaPom = AgendaPOM(device)
            val speakersPom = SpeakersPOM(device)
            val networkingPom = NetworkingPOM(device)
            val partnersPom = PartnersPOM(device)
            val infoPom = InfoPOM(device)

            pressHome()
            startActivityAndWait()

            agendaPom.waitDataFetched()

            with(infoPom) {
                open()
                expandOverflowMenu()
                changeEventAction()
            }

            with(eventListPom) {
                goToPastEvents()
                clickDevfest()
            }

            with(agendaPom) {
                open()
                openFilters()
                back()
                openFirstItem()
                back()
                scrollUpDown()
            }

            with(speakersPom) {
                open()
                openFirstItem()
                back()
                scrollUpDown()
            }

            with(networkingPom) {
                open()
            }

            with(partnersPom) {
                open()
                openFirstItem()
                back()
                scrollUpDown()
            }

            with(infoPom) {
                open()
                expandOverflowMenu()
                changeEventAction()
            }
        }
    }
}
