package com.codingub.emergency.data.remote.datasource

import android.util.Log
import com.codingub.emergency.data.remote.models.ArticleResponse
import com.codingub.emergency.data.remote.models.ServiceResponse
import com.codingub.emergency.data.utils.Constants.ARTICLE_COLLECTION
import com.codingub.emergency.data.utils.Constants.SERVICE_COLLECTION
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FireDataSource {


    override suspend fun getArticles(): List<Article> {
        return db.collection(ARTICLE_COLLECTION).get().await().map { document ->
            ArticleResponse(
                id = document.id,
                item = document.toObject(ArticleResponse.ArticleItem::class.java)
            ).toArticle()
        }
    }

    override suspend fun searchArticles(alt: String): List<Article> {
        val articles = mutableListOf<Article>()
        db.collection(ARTICLE_COLLECTION).get().await().map { document ->
            val docAlt = document.getString("alt")
            docAlt?.let {
                if (docAlt.checkWordInString(alt)) {
                    articles.add(
                        ArticleResponse(
                            id = document.id,
                            item = document.toObject(ArticleResponse.ArticleItem::class.java)
                        ).toArticle()
                    )
                }
            }
        }
        return articles
    }

    override suspend fun getServicesFromLanguage(language: String): List<Service> {
        return db.collection(SERVICE_COLLECTION)
            .whereEqualTo("language", language)
            .get()
            .await()
            .map { document ->
                ServiceResponse(
                    id = document.id,
                    item = document.toObject(ServiceResponse.ServiceItem::class.java)
                ).toService()
            }
    }
}

/*
    Additional
 */

fun String.checkWordInString(alt: String): Boolean {
    val words = this.split("\\s+".toRegex())
    Log.d("words", words.toString())
    return alt in words
}