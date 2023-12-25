package com.codingub.emergency.stub

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.mockResponses(vararg pairs: Pair<String, MockContainer>) {
    val mockUtils = MockUtils()
    val requestResponseMap = mockUtils.baseRequestResponseMap
    for (pair in pairs) {
        requestResponseMap[pair.first] = pair.second.getResponse()
    }
    dispatcher = mockUtils.createDispatcher(requestResponseMap)
}

/**
 * [ModelContainer] – для модели, сериализуемой в строку тела ответа
 * [KeyValueContainer] – для Key-Value-объекта, сериализуемого в строку тела ответа
 * [RawStringContainer] – для тела ответа в виде уже готовой строки
 * [AssetContainer] – для тела ответа, которое хранится в assets
 * [AnyContainer] – контейнер, который позволит создать произвольный [MockResponse].
 * Может быть полезен при тестировании негативных сценариев
 */

sealed interface MockContainer {
    fun getResponse(): MockResponse
}

class AnyContainer(private val mockResponse: MockResponse) : MockContainer {
    override fun getResponse() = mockResponse
}

class AssetContainer(private val fileName: String) : MockContainer {
    override fun getResponse() = mockUtils.successResponse(fileName)
}

class RawStringContainer(private val body: String) : MockContainer {
    override fun getResponse() = mockUtils.successResponse { body }
}

class ModelContainerClass<T>(private val model: T, private val serializer: KSerializer<T>) : MockContainer {
    override fun getResponse() = mockUtils.successResponse { json.encodeToString(serializer, model) }
}

class KeyValueContainer(private val params: Any) : MockContainer {
    override fun getResponse() = mockUtils.successResponse { params.toJsonElement().toString() }
}

@Suppress("TestFunctionName")
inline fun <reified T> ModelContainer(model: T): ModelContainerClass<T> =
    ModelContainerClass(model, Json.serializersModule.serializer())

val mockUtils: MockUtils = MockUtils()
private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}
