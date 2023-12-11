package com.codingub.emergency.data.local.datasource

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
    override suspend fun insertArticles(articles: List<Article>) {
        return database.withTransaction {
            dao.deleteAllArticles()
            dao.insertArticles(articles.map {
                it.toArticleEntity()
            })
        }
    }


    override suspend fun updateFavoriteArticle(article: Article) {
        val exArticle = dao.getFavoriteArticle(article.id)
        if (exArticle != null) {
            dao.deleteFavoriteArticle(article.id)
            return
        }
        dao.insertFavoriteArticle(article.toFavoriteArticle())
    }

    override fun getFavoriteArticles(): Flow<List<Article>> {
        return dao.getFavoriteArticles().map {
            it.map { article ->
                article.toArticle()
            }
        }
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return dao.getArticles().map {
            it.map { article -> article.toArticle() }
        }
    }

    override fun getArticle(id: String): Flow<Article> {
        return dao.getArticle(id).map { it.toArticle() }
    }

    override suspend fun isFavoriteArticle(id: String): Boolean {
        return dao.getFavoriteArticle(id) != null
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