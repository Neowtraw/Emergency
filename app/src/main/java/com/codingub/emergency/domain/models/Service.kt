package com.codingub.emergency.domain.models

import com.codingub.emergency.data.local.models.ServiceEntity

data class Service(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val language: String = ""
) {

    fun toServiceEntity() =
        ServiceEntity(
            id = id,
            name = name,
            phone = phone,
            language = language
        )
}
