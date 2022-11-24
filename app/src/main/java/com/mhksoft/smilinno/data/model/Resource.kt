package com.mhksoft.smilinno.data.model

import androidx.annotation.Keep
import retrofit2.HttpException

@Keep
data class Resource<T>
    (
    val state: ResourceState = ResourceState.LOADING,
    val data: T? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun <T> success(resource: T): Resource<T?> {
            return Resource(ResourceState.SUCCESS, data = resource)
        }

        fun <T> error(errorMessage: String): Resource<T?> {
            return Resource(ResourceState.ERROR, errorMessage = errorMessage)
        }

        /**
         * Handles http errors
         */
        fun <T> error(httpCode: Int): Resource<T?> {
            return when (httpCode) {
                else -> error("خطا در شبکه داخلی")
            }
        }

        /**
         * Handles local throwable errors
         */
        fun <T> error(throwable: Throwable): Resource<T?> {
            throwable.printStackTrace()
            return if (throwable is HttpException) {
                error(throwable.code())
            } else error("خطا در اتصال به شبکه، اگر از فیلتر شکن استفاده می کنید، آن را خاموش نمایید")
        }

        fun <T> loading(): Resource<T?> {
            return Resource(ResourceState.LOADING)
        }
    }
}

enum class ResourceState {
    SUCCESS, ERROR, LOADING
}