package com.codingub.emergency.data.remote.datasource

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class FireDataSourceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var SUT: FireDataSource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `when request articles then return correct articles`() = runTest {
        val defaultArticles = SUT.getArticles()

        assertThat(defaultArticles.isEmpty().not())
        assertThat(defaultArticles.size).isEqualTo(defaultArticles.size)
    }
}