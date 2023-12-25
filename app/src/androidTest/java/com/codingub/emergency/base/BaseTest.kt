package com.codingub.emergency.base

import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import okhttp3.mockwebserver.MockWebServer

abstract class BaseTest(
    protected val server: MockWebServer = MockWebServer(),
    builder: Kaspresso.Builder = Kaspresso.Builder.simple {
        beforeEachTest {
            server.start(8080)
        }
        afterEachTest {
            server.shutdown()
        }
    },
) : TestCase(builder) {

//    @get:Rule
//    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
}
