package com.mhksoft.smilinno.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Flow<Resource<T?>> {
        return flow {
            emit(Resource.loading())
            try {
                val response = call()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null)
                        emit(Resource.success(body))
                } else
                    emit(Resource.error(response.code()))
            } catch (e: Exception) {
                emit(Resource.error(e))
            }
        }
    }
}