package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.core.Resource
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.data.remote.models.ArticleResponse
import com.codingub.emergency.data.remote.models.ContentResponse
import com.codingub.emergency.data.remote.models.ServiceResponse
import com.codingub.emergency.data.utils.Constants.ARTICLE_COLLECTION
import com.codingub.emergency.data.utils.Constants.SERVICE_COLLECTION
import com.codingub.emergency.data.utils.NetworkLostException
import com.codingub.emergency.data.utils.NoResultsException
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class FireDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val resources: Resource
) : FireDataSource {


    override fun getArticles(): Flow<ResultState<List<Article>>> = callbackFlow {
        db.collection(ARTICLE_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                val articles = mutableListOf<Article>()
                // wrap documents
                val articleTasks = snapshot.documents.map { document ->

                    //get content
                    val contentRefs = document["content"] as? List<DocumentReference> ?: emptyList()
                    val content = mutableListOf<ContentResponse>()
                    val contentTasks = contentRefs.map { contentRef ->
                        contentRef.get()
                            .addOnSuccessListener { contentDocument ->
                               content.add(contentDocument.toContentResponse())
                            }
                            .addOnFailureListener { error ->
                                trySend(ResultState.Error(error))
                            }
                    }
                    //await while all content received and get article
                    Tasks.whenAll(contentTasks)
                        .addOnSuccessListener {
                           articles.add(ArticleResponse(
                                id = document.id,
                                item = document.toObject(ArticleResponse.ArticleItem::class.java)
                                    ?: throw NoResultsException(resources),
                                content = content
                            ).toArticle()
                           )
                        }
                        .addOnFailureListener { error ->
                            trySend(ResultState.Error(error))
                        }
                }
                // when all articles are prepared, send
                Tasks.whenAll(articleTasks)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(articles))
                        close()
                    }
                    .addOnFailureListener { error ->
                        trySend(ResultState.Error(error))
                    }
            }
            .addOnFailureListener { error ->
                trySend(ResultState.Error(NetworkLostException(resources)))
            }

        awaitClose { close() }
    }


    override fun getServicesFromLanguage(language: String): Flow<ResultState<List<Service>>> = callbackFlow {
        db.collection(SERVICE_COLLECTION)
            .whereEqualTo("language", language)
            .get()
            .addOnSuccessListener { snapshot ->
                val services = snapshot.documents.map { document ->
                    ServiceResponse(
                        id = document.id,
                        item = document.toObject(ServiceResponse.ServiceItem::class.java)
                            ?: throw NoResultsException(resources)
                    ).toService()
                }
                trySend(ResultState.Success(services))
                close()
            }
            .addOnFailureListener {
                trySend(ResultState.Error(NetworkLostException(resources)))
            }

        awaitClose { close() }
    }
}

/*
    Additional
 */

fun DocumentSnapshot.toContentResponse(): ContentResponse {

    return ContentResponse(
        id = this.id,
        item = this.toObject(ContentResponse.ContentItem::class.java)
            ?: ContentResponse.ContentItem()
    )
}

