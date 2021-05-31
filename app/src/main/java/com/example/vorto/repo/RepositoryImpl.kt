package com.example.vorto.repo

import com.example.vorto.model.BaseResposne
import com.example.vorto.model.ResponseObject
import com.example.vorto.model.ServiceException
import com.example.vorto.model.ServiceResult
import com.example.vorto.service.ApiService
import com.example.vorto.service.RetrofitBase
import com.example.vorto.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class RepositoryImpl() : Repository {

    private val apiService: ApiService = RetrofitBase.getApiService(Utils.base_Url)
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getBusinessContacts(query: String,latitude:Double,longitude:Double): ServiceResult<ResponseObject> {
        val result = withContext(ioDispatcher)
        {

            processCall {
                apiService.getBusiness(Utils.yelp_api_key, query,latitude,longitude)
            }
        }
        return when (result) {
            is ServiceResult.Success -> transformResponse(result.data)
            is ServiceResult.Error -> result
        }
    }

    internal fun transformResponse(response: BaseResposne): ServiceResult<BaseResposne> {
        response.apply {
            return if (!errorCode.isNullOrEmpty())
                ServiceResult.Error(
                    ServiceException(
                        response.code,
                        errorCode.firstOrNull().toString(),
                        errorMessage.firstOrNull().orEmpty()
                    )
                )
            else
                ServiceResult.Success(response)
        }
    }

    suspend fun <T> processCall(
        call: suspend () -> Response<T>
    ): ServiceResult<T> {
        val networkError = ServiceException(201, "201", "Sorry. Internet might not be connected.")
        return try {
            val serviceCallback = call()
            val body = serviceCallback.body()
            if (serviceCallback.isSuccessful && body != null) {
                ServiceResult.Success(body)
            } else if (!serviceCallback.isSuccessful && serviceCallback.errorBody() != null) {
                ServiceResult.Error(processErrorBody(serviceCallback))
            } else {
                ServiceResult.Error(
                    ServiceException(
                        serviceCallback.code(),
                        serviceCallback.code().toString(),
                        serviceCallback.message().orEmpty()
                    )
                )
            }

        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    ServiceResult.Error(networkError)
                }
                else -> {
                    ServiceResult.Error(ServiceException(101, "101", exception.toString()))
                }
            }
        }
    }

    private fun <T> processErrorBody(response: Response<T>): ServiceException {
        val type = object : TypeToken<BaseResposne>() {}.type
        val errorResponse: BaseResposne? = Gson().fromJson(response.errorBody()?.charStream(), type)

        if (!errorResponse?.responseMessage.isNullOrEmpty()) {
            return ServiceException(
                response.code(),
                errorResponse?.httpStatus.toString(),
                errorResponse?.responseMessage.toString()
            )

        } else {
            return ServiceException(
                response.code(),
                errorResponse?.errorCode?.first().toString(),
                errorResponse?.errorMessage?.first().toString()
            )
        }
    }

}