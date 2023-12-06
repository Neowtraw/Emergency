package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.data.remote.responses.ArticleResponse
import com.codingub.emergency.data.utils.Constants
import com.codingub.emergency.domain.models.Article
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FireDataSourceImpl @Inject constructor(
   private val db: FirebaseFirestore
) : FireDataSource {


    override fun getArticles(): Flow<ResultState<List<Article>>> = callbackFlow {
        trySend(ResultState.Loading())
        db.collection(Constants.ARTICLE_COLLECTION)
            .get()
            .addOnSuccessListener { response ->
                val items = response.map { data ->
                    ArticleResponse(
                        item = ArticleResponse.ArticleItem(
                            title = data["title"] as String,
                            description = data["description"] as String,
                            summary = data["summary"] as String,
                            imageUrl = data["imageUrl"] as String,
                            videoUrl = data["videoUrl"] as String?,
                            phone = data["phone"] as String?,
                            alt = data["alt"] as String
                        ),
                        id = data.id
                    )
                }
                trySend(ResultState.Success(items.map { it.toArticle() }))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }
    }

    override fun searchArticles(alt: String): Flow<ResultState<List<Article>>> =
        callbackFlow {
            trySend(ResultState.Loading())

            val filteredDocuments = mutableListOf<DocumentSnapshot>()

            db.collection(Constants.ARTICLE_COLLECTION)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val docAlt = document.getString("alt")
                        docAlt?.let {
                            if (isSimilar(docAlt, alt)) {
                                filteredDocuments.add(document)
                            }
                        }
                    }
                    val items = documents.map { data ->
                        ArticleResponse(
                            item = ArticleResponse.ArticleItem(
                                title = data["title"] as String,
                                description = data["description"] as String,
                                summary = data["summary"] as String,
                                imageUrl = data["imageUrl"] as String,
                                videoUrl = data["videoUrl"] as String?,
                                phone = data["phone"] as String?,
                                alt = data["alt"] as String
                            ),
                            id = data.id
                        )
                    }
                    trySend(ResultState.Success(items.map { it.toArticle() }))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it))
                }
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