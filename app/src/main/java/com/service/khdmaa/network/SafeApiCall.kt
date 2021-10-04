package com.service.khdmaa.network

import android.util.Log
import com.service.khdmaa.utiles.Utiles
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject


suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(Dispatchers.IO) {
        try {

            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Utiles.log_D("dndndndndndnd", "$throwable")
            getErrorType(throwable)

        }
    }
}

public fun <T> getErrorType(throwable: Throwable): ResultWrapper<T> {
    when (throwable) {
        is IOException, is UnknownHostException -> {
            return ResultWrapper.NetworkError
        }
        is HttpException -> {
            val code = throwable.code()

            val errorResponse = convertErrorBody(throwable)
            return ResultWrapper.GenericError(code, errorResponse)
        }
        else -> {
            return ResultWrapper.GenericError(0, ErrorResponse())
        }
    }
    return ResultWrapper.GenericError(0, ErrorResponse())
}


private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)

        }

    } catch (exception: Exception) {

        ErrorResponse("some thing error")
    }
}


sealed class ResultWrapper<out T>() {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()

}

data class ErrorResponse(val message: String = "some thing error, try again")
