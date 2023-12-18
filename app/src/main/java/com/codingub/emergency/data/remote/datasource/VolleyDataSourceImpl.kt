package com.codingub.emergency.data.remote.datasource

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.codingub.emergency.data.remote.models.NewsDao
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jsoup.Jsoup
import kotlin.coroutines.resume

class NoResultsException() : Exception()

class VolleyDataSourceImpl : VolleyDataSource {

    companion object {
        const val NEWS_URL = "https://mchs.gov.by/glavnoe/"
        const val NEWS_CLASS = "news-links__item"
    }

    override suspend fun getTheLastNewsLink(): String =
        suspendCancellableCoroutine { continuation ->
            StringRequest(
                Request.Method.GET, NEWS_URL, { response ->
                    try {
                        val doc = Jsoup.parse(response)
                        val data = doc.getElementsByClass(NEWS_CLASS).first()?.attr("href")
                            ?: throw NoResultsException()

                        continuation.resume(data)
                    } catch (e: Exception) {
                        continuation.cancel(NoResultsException())
                    }
                }, {
                    continuation.cancel(NoResultsException())
                }
            )
        }

    override suspend fun getTheLastNews(href: String): NewsDao =
        suspendCancellableCoroutine { continuation ->
            StringRequest(
                Request.Method.GET, NEWS_URL, { response ->
                    try {
                        val doc = Jsoup.parse(response)
                        val data = doc.getElementsByAttribute("head").first()
                            ?.getElementsByAttribute("meta") ?: throw NoResultsException()


                        continuation.resume(
                            NewsDao(
                                imageUrl = data.attr("property", "og:image").attr("content") ?: "",
                                title = data.attr("property", "og:title").attr("content") ?: "",
                                description = data.attr("property", "og:description")
                                    .attr("content") ?: "",
                                link = data.attr("property", "og:url").attr("content") ?: ""
                            )
                        )

                    } catch (e: Exception) {
                        continuation.cancel(NoResultsException())
                    }
                },
                {
                    continuation.cancel(NoResultsException())
                }
            )

        }
}