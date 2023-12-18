package com.codingub.emergency.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val NOTIFICATION_SEND_KEY = "NOTIFICATION_SENT"
const val WORKER_TAG = "NOTIFICATION_WORKER_TAG"
const val WORKER_NAME = "NOTIFICATION_WORKER_NAME"

@HiltWorker
class NotificationSenderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}