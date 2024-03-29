package com.codingub.emergency.data.remote.datasource

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.codingub.emergency.core.Resource
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.data.utils.NoResultsException
import com.codingub.emergency.domain.models.News
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.coroutines.resume


class VolleyDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resource: Resource,
    private val datastore: DataStoreRepository
) :
    VolleyDataSource {

    companion object {
        const val NEWS_URL = "https://minobl.mchs.gov.by/novosti/"
    }

    override suspend fun getTheLastNewsLink(): String {
        val queue = Volley.newRequestQueue(context)

        return suspendCancellableCoroutine { continuation ->
            StringRequest(
                Request.Method.GET, NEWS_URL, { response ->
                    try {
                        val doc = Jsoup.parse(response)
                        val data = doc.select(
                            "body > div.wrapper > div > div > div > div > div.main-inside " +
                                    "> div.content-layout > div.content-wrap > div " +
                                    "> div.content-news > section:nth-child(4) > div.section__holder " +
                                    "> div:nth-child(1) > div:nth-child(1) > div > a"
                        ).attr("href") ?: throw NoResultsException(resource)

                        continuation.resume("https://minobl.mchs.gov.by$data")
                    } catch (e: Exception) {
                        continuation.cancel(NoResultsException(resource))
                    }
                }, {
                    continuation.cancel(Exception())
                }
            ).also {
                queue.add(it)
            }
        }.also {
            if (it == datastore.readLastNewsLink().first()) datastore.saveLastNewsLink(it)
        }
    }

    override suspend fun getTheLastNews(href: String): News {
        val queue = Volley.newRequestQueue(context)

        return suspendCancellableCoroutine { continuation ->
            StringRequest(
                Request.Method.GET, href, { response ->
                    try {
                        val doc = Jsoup.parse(response)

                        continuation.resume(
                            News(
                                imageUrl = doc.select("head > meta:nth-child(9)").attr("content")
                                    ?: "",
                                title = doc.select(
                                    "body > div.wrapper > div > div > div > div > div.main-inside >" +
                                            " div.content-layout > div.content-wrap > div > div > div > h1"
                                ).text() ?: "",
                                description = doc.select(
                                    "body > div.wrapper > div > div > div > div > div.main-inside > " +
                                            "div.content-layout > div.content-wrap > div > div > div > p:nth-child(5)"
                                ).text() ?: "",
                                link = doc.select(
                                    "head > meta:nth-child(11)"
                                ).attr("content")
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("VolleyError1", "Error: ${e.message}")
                        continuation.cancel(NoResultsException(resource))
                    }
                },
                {
                    Log.e("VolleyError", "Error: $it")
                    continuation.cancel(NoResultsException(resource))
                }
            ).also {
                queue.add(it)
            }
        }
    }
}