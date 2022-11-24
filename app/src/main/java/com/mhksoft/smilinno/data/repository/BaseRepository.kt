package com.mhksoft.smilinno.data.repository

import com.mhksoft.smilinno.data.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Flow<Resource<T?>> {
        return flow {
            emit(Resource.loading())
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)
                    emit(Resource.success(body))
            } else
                emit(Resource.error(response.code()))
        }.catch {
            emit(Resource.error(it))
        }.flowOn(Dispatchers.IO)
    }
}