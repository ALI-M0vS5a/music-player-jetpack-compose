package com.example.musicplayer.other

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): Resources<T> = try {
    val response = call()
    if (response.isSuccessful)
        response.body()?.let { Resources.Success(data = it) }
            ?: Resources.Error(message = "Data is null")
    else
        Resources.Error(message = response.message())

} catch (e: IOException) {
    Resources.Error(ErrorType.NO_INTERNET)
} catch (e: Exception) {
    Resources.Error(ErrorType.UNKNOWN)
}

suspend fun <T> safeCall(
    handleNullCheck: Boolean = true,
    call: suspend () -> T
): Resources<T> = try {
    val response = call()
    if (handleNullCheck) handleNullCheck(response)
    else Resources.Success(data = response)
} catch (e: IOException) {
    Resources.Error(ErrorType.NO_INTERNET)
} catch (e: Exception) {
    Resources.Error(ErrorType.UNKNOWN, message = e.message.toString())
}

fun <T> handleNullCheck(data: T?): Resources<T> = data?.let {
    Resources.Success(data = it)
} ?: Resources.Error(message = "Data is null")
