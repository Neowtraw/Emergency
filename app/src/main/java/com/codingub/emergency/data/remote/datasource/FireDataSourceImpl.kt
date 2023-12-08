package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.data.remote.responses.ArticleResponse
import com.codingub.emergency.data.utils.Constants
import com.codingub.emergency.data.utils.Constants.ARTICLE_COLLECTION
import com.codingub.emergency.domain.models.Article
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
        db.collection(Constants.ARTICLE_COLLECTION).get().await().map { document ->
            val docAlt = document.getString("alt")
            docAlt?.let {
                if (isSimilar(docAlt, alt)) {
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

    private fun isSimilar(userInput: String, alt: String?): Boolean {
        if (alt.isNullOrBlank()) return false

        val n = userInput.length
        val m = alt.length

        val dp = Array(n + 1) { IntArray(m + 1) }
        for (i in 0..n) {
            for (j in 0..m) {
                if (i == 0) {
                    dp[i][j] = j
                } else if (j == 0) {
                    dp[i][j] = i
                } else if (userInput[i - 1] == alt[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1]
                } else {
                    dp[i][j] = 1 + minOf(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1])
                }
            }
        }
        val levenshteinDistance = dp[n][m]
        val similarityThreshold = 2
        return levenshteinDistance <= similarityThreshold
    }
}