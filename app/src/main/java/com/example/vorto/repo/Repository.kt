package com.example.vorto.repo

import com.example.vorto.model.ResponseObject
import com.example.vorto.model.ServiceResult

interface Repository {

    suspend fun getBusinessContacts(query:String,latitude:Double,longitude:Double):ServiceResult<ResponseObject>
}