package com.example.vorto.model

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

sealed class ResponseObject

@Parcelize
open class BaseResposne(
    val code: Int = 0,
    val errorCode: ArrayList<Int> = ArrayList(),
    val errorMessage: ArrayList<String> = ArrayList(),
    val responseMessage: String? = null,
    val httpStatus: Int = 0
) : ResponseObject(), Parcelable

@Parcelize
data class BusinessResponse(
    @SerializedName("businesses") val businesses: ArrayList<Businesses>?,
    val total: Int?,
    val region: Region
) : BaseResposne(), Parcelable

@Parcelize
data class Region(val center: Center):Parcelable

@Parcelize
data class Center(val latitude: Double, val longitude: Double):Parcelable

@Parcelize
data class Businesses(
    val rating: Float?,
    val phone: String?,
    val is_closed: Boolean,
    val coordinates: Coordinates,
    val location: Location,
    val distance: Double?,
    val name: String?,
    val url: String?,
    val image_url:String?
):Parcelable

@Parcelize
data class Coordinates(val latitude: Double, val longitude: Double):Parcelable

@Parcelize
data class Location(
    val city: String,
    val country: String,
    val address1: String,
    val address2: String,
    val state: String,
    val zip_code: String
):Parcelable