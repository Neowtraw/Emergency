package com.codingub.emergency.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.ArticleRef
import com.codingub.emergency.data.local.models.ContentEntity
import com.codingub.emergency.data.local.models.ServiceEntity
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    /*
        Article
     */

    @Transaction
    @Query("SELECT * FROM Article")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Transaction
    @Query("SELECT * FROM Article WHERE liked = 1")
    fun getFavoriteArticles(): Flow<List<ArticleEntity>>

    @Transaction
    @Query("SELECT * FROM Article WHERE alt LIKE '%' || :alt || '%'")
    fun searchArticles(alt: String) : Flow<List<ArticleEntity>>

    @Transaction
    @Query("SELECT * FROM Article WHERE id = :articleId")
    fun getArticle(articleId: String): Flow<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<ArticleRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: List<ContentEntity>)

    @Query("DELETE FROM Article WHERE liked = 0")
    suspend fun deleteAllArticles()

    @Query("UPDATE Article SET liked = 1 WHERE id = :id")
    suspend fun addArticleToFavoriteById(id: String)

    @Query("UPDATE Article SET liked = 0 WHERE id = :id")
    suspend fun removeArticleFromFavoriteById(id: String)

    /*
        Service
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Query("SELECT * FROM Service")
    fun getSavedServices(): Flow<List<Service>>

    @Query("DELETE FROM Service")
    fun deleteSavedServices()
}