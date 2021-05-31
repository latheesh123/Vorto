package com.example.vorto.model

sealed class ServiceResult<out R>{
    data class Success<out T>(val data:T):ServiceResult<T>()
    data class Error(val error:ServiceException):ServiceResult<Nothing>()
}