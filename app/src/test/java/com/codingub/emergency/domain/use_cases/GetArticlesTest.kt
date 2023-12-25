package com.codingub.emergency.domain.use_cases

import com.MockitoHelper.mock
import com.MockitoHelper.whenever
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.repos.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetArticlesTest {

    @Mock
    private lateinit var repos: AppRepository

    private lateinit var SUT: GetArticles

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetArticles(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when get articles then correct articles`() = runTest {
        // initialize
        val articlesResult: ResultState<List<Article>> =
            ResultState.Success(listOf(mock(), mock(), mock()))

        // when
        whenever(repos.getArticles()).thenReturn(flowOf(articlesResult))
        val result = SUT.invoke().first()

        // then
        verify(repos, times(1)).getArticles()
        assertThat(articlesResult).isEqualTo(result)
    }
}