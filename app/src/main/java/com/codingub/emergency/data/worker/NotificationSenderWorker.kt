package com.codingub.emergency.data.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import com.codingub.emergency.R
import com.codingub.emergency.data.remote.datasource.VolleyDataSource
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.domain.models.News
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val NOTIFICATION_SEND_KEY = "NOTIFICATION_SENT"
const val WORKER_TAG = "NOTIFICATION_WORKER_TAG"
const val WORKER_NAME = "NOTIFICATION_WORKER_NAME"
const val CHANNEL_ID = "NOTIFICATION_WORKER_CHANNEL_ID"
const val NOTIFICATION_ID = "NOTIFICATION_WORKER_ID"

@HiltWorker
class NotificationSenderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val volleyDataSource: VolleyDataSource,
    private val datastore: DataStoreRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            volleyDataSource.getTheLastNewsLink().also {
                if (datastore.readLastNewsLink().asLiveData().value != it) {
                    displayNotification(volleyDataSource.getTheLastNews(href = it))

                }
            }
            Result.success(workDataOf(NOTIFICATION_SEND_KEY to true))
        } catch (e: Exception) {
            Log.d("test-link", "$e")
            Result.failure(workDataOf(NOTIFICATION_SEND_KEY to false))
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun displayNotification(news: News) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val imageLoader = ImageLoader(context)
        ImageRequest.Builder(context)
            .data(news.imageUrl)
            .target { drawable ->
                val image = drawable.toBitmap()
                val vibratePattern = longArrayOf(0, 100, 200, 300)

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(news.title)
                    .setContentText(news.description)
                    .setSmallIcon(R.drawable.ic_articles)
                    .setLargeIcon(image)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(vibratePattern)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

                with(NotificationManagerCompat.from(context)) {
                    notify(0, notification)
                }
            }
            .build().also {
                imageLoader.enqueue(it)
            }
    }
}
