package com.codingub.emergency.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.FavoriteArticleEntity
import com.codingub.emergency.data.local.models.ServiceEntity
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao  {

    /*
        Article
     */

    @Query("SELECT * FROM Article")
    fun getArticles() : Flow<List<ArticleEntity>>

    @Query("SELECT * FROM Article WHERE id = :articleId")
    fun getArticle(articleId: String) : Flow<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM Article")
    suspend fun deleteAllArticles()

    /*
        FavoriteArticle
     */
    @Query("SELECT * FROM FavoriteArticle")
    fun getFavoriteArticles() : Flow<List<FavoriteArticleEntity>>

    @Query("SELECT * FROM FavoriteArticle WHERE id = :id")
    suspend fun getFavoriteArticle(id: String) : FavoriteArticleEntity?

    @Insert
    suspend fun insertFavoriteArticle(article: FavoriteArticleEntity)

    @Query("DELETE FROM FavoriteArticle WHERE id = :articleId")
    suspend fun deleteFavoriteArticle(articleId: String)

    /*
        Service
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

        @Query("SELECT * FROM Service")
        fun getSavedServices() : Flow<List<Service>>

    @Query("DELETE FROM Service")
    fun deleteSavedServices()
}