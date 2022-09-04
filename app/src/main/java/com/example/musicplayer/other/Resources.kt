package com.example.musicplayer.other


sealed class Resources<T>(
    open val data: T? = null,
    open val message: String = "",
    open val errorType: ErrorType = ErrorType.UNKNOWN
){
    class Loading<T>() : Resources<T>()

    data class Success<T>(override val data: T?, override val message: String = "") :
            Resources<T>(data, message)

    data class Error<T>(
        override val errorType: ErrorType = ErrorType.UNKNOWN,
        override val message: String = errorType.errorMessage
    ) : Resources<T>(null, message, errorType)

}
enum class ErrorType(val errorMessage: String) {
    NO_INTERNET("Looks like you don't have an internet connection"),
    UNKNOWN("Oops something went wrong")
}