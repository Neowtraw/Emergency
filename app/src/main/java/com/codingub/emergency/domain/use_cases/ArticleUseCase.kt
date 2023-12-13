package com.codingub.emergency.domain.use_cases

import com.codingub.emergency.domain.repos.AppRepository
import javax.inject.Inject

class GetArticles @Inject constructor(
    private val rep: AppRepository
) {
    operator fun invoke() = rep.getArticles()
}

class GetArticle @Inject constructor(
    private val rep: AppRepository
) {
    operator fun invoke(id: String) = rep.getArticle(id)
}

class GetFavoriteArticles @Inject constructor(
    private val rep: AppRepository
) {
    operator fun invoke() = rep.getFavoriteArticles()
}


class SearchArticles @Inject constructor(
    private val rep: AppRepository
) {
    operator fun invoke(alt: String) = rep.searchArticles(alt)
}

class UpdateFavoriteArticle @Inject constructor(
    private val rep: AppRepository
) {
    suspend operator fun invoke(id: String, liked: Boolean) = rep.updateFavoriteArticle(id, liked)
}
