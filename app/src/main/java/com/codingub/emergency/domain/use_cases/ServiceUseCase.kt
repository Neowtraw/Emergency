package com.codingub.emergency.domain.use_cases

import com.codingub.emergency.domain.repos.AppRepository
import javax.inject.Inject

class GetServicesFromLanguage @Inject constructor(
    private val repository: AppRepository
) {
    operator fun invoke(language: String) = repository.getServicesFromLanguage(language)
}