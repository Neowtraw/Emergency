package com.codingub.emergency.data.local.datasource

import android.util.Log
import androidx.room.withTransaction
import com.codingub.emergency.data.local.room.RoomDao
import com.codingub.emergency.data.local.room.RoomDatabase
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val database: RoomDatabase,
    private val dao: RoomDao
) : LocalDataSource {

    override suspend fun updateFavoriteArticle(id: String, liked: Boolean) {
        if(liked) {
            dao.removeArticleFromFavoriteById(id)
            return
        }
        dao.addArticleToFavoriteById(id)
    }

    override fun getFavoriteArticles(): Flow<List<Article>> {
        return dao.getFavoriteArticles().map { articles ->
            articles.map { it.toArticle() }
        }
    }

    override suspend fun insertArticles(articles: List<Article>) {
        return database.withTransaction {
            dao.deleteAllArticles()
            dao.insertArticles(articles.map {
                it.toArticleRef()
            })
            dao.insertContent(articles.flatMap {
                it.content.map { contentItem ->
                    contentItem.toContentEntity()
                }
            })
        }
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return dao.getArticles().map {
            it.map { article -> article.toArticle() }
        }
    }

    override fun searchArticles(alt: String): Flow<List<Article>> {
        return dao.searchArticles(alt).map {
            it.map { article -> article.toArticle() }
        }
    }

    override fun getArticle(id: String): Flow<Article> {
        return dao.getArticle(id).map { it.toArticle() }
    }

    override suspend fun insertServices(services: List<Service>) {
        return database.withTransaction {
            dao.deleteSavedServices()
            dao.insertServices(services.map {
                it.toServiceEntity()
            })
        }

    }

    override fun getSavedServices(): Flow<List<Service>> {
        return dao.getSavedServices()
    }
}

fun String.checkWordInString(alt: String): Boolean {
    val words = this.split("\\s+".toRegex())
    Log.d("words", words.toString())
    return alt in words
}