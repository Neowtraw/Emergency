package com.codingub.emergency.domain.use_cases

import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SetUserInformation @Inject constructor(
    private val repository: DataStoreRepository,
    private val auth: FirebaseAuth
) {

    suspend operator fun invoke(username: String, address: String, parentPhone: String?, age: Int) {
        repository.saveUserInfo(
            User(
            username = username,
                phone = auth.currentUser?.phoneNumber ?: "",
                address = address,
                age = age,
                parentNumber = parentPhone
        )
        )
    }
}

class GetUserInformation @Inject constructor(
    private val repository: DataStoreRepository
) {

    suspend operator fun invoke() : User? {
        return repository.readUserInfo().firstOrNull()
    }
}