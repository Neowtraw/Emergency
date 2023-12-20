package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.codingub.emergency.data.worker.NotificationSenderWorker
import com.codingub.emergency.data.worker.WORKER_NAME
import com.codingub.emergency.data.worker.WORKER_TAG
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.use_cases.GetFavoriteArticles
import com.codingub.emergency.domain.use_cases.UpdateFavoriteArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


const val UPDATE_INTERVAL = 1L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFavoriteArticles: GetFavoriteArticles,
    private val updateFavoriteArticle: UpdateFavoriteArticle,
    private val workManager: WorkManager
) : ViewModel() {

    private val _articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()


    init {
        getArticles()
    }

    fun initializeWorkManager() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationSenderWorker>(
            15L, TimeUnit.MINUTES
        )
            .addTag(WORKER_TAG)
            .setConstraints(
                Constraints(requiredNetworkType = NetworkType.CONNECTED)
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }


    fun updateFavoriteArticles(id: String, liked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteArticle(id, liked)
        }
    }

    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteArticles().collect { arts ->
                _articles.value = arts
            }
        }
    }
}