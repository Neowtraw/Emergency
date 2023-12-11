package com.codingub.emergency.data.remote.responses

import com.codingub.emergency.domain.models.Service

data class ServiceResponse(
    val id: String,
    val item: ServiceItem
){

    data class ServiceItem(
        val name: String = "",
        val phone: String = "",
        val language: String = ""
    )

    fun toService() =
        Service(
            id = id,
            name = item.name,
            phone = item.phone,
            language = item.language
        )
}