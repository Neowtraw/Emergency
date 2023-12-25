package com.codingub.emergency.stub

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.net.HttpURLConnection

class MockUtils {
    companion object {
        const val SOME_REQUEST_PATH = "/api/v1/hello/world"

        const val SOME_RESPONSE_FILE = "hello/world_pretty.json"
    }

    val jsonUtil = JsonUtil(appContext as Application)

    /**
     * Набор стабов, который будет использован по умолчанию,
     * чтобы начать тест с нужного экрана приложения без сетевых ошибок в значимых эндпоинтах
     */
    val baseRequestResponseMap: HashMap<String, MockResponse?> = hashMapOf(
        SOME_REQUEST_PATH to successResponse(SOME_RESPONSE_FILE),
    )

    /**
     * Коллекция [mockResponsesMap] это Path конкретных эндпоинтов и соответствующих им стабов.
     *
     * Если [MockWebServer] увидит запрос для Path, которого нет среди ключей [mockResponsesMap],
     * то будет возвращена ошибка [HttpURLConnection.HTTP_NOT_FOUND].
     *
     * (!) В текущей реализации можно использовать ключи,
     * которые соответствуют окончанию эндпоинта (endsWith)
     */
    fun createDispatcher(
        mockResponsesMap: Map<String, MockResponse?>
    ) = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            mockResponsesMap.forEach {
                if (request.path?.endsWith(it.key) == true) {
                    return mockResponsesMap[it.key] ?: failNotFoundResponseMock()
                }
            }
            return failNotFoundResponseMock()
        }
    }

    fun failNotFoundResponseMock(): MockResponse =
        MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)

    fun successResponse(fileName: String): MockResponse =
        MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonUtil.readJSONFromAsset(fileName))

    fun successResponse(body: () -> String): MockResponse =
        MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(body.invoke())
}

class JsonUtil(
    private val application: Application
) {
    fun readJSONFromAsset(fileName: String): String {
        return try {
            val inputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
    }
}

val targetContext: Context
    get() = InstrumentationRegistry.getInstrumentation().targetContext

val appContext: Context
    get() = targetContext.applicationContext
