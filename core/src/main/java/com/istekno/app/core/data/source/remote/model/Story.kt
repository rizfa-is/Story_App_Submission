package com.istekno.app.core.data.source.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import okhttp3.RequestBody

object Story {
    data class Request(
        var photo: MultipartBody.Part? = null,
        var description: RequestBody? = null,
        var lat: RequestBody? = null,
        var lon: RequestBody? = null
    )
    
    data class Response(
        val error: Boolean,
        val message: String,
        val listStory: List<Data>
    ) {
        @Parcelize
        data class Data(
            val id: String,
            val name: String,
            val description: String,
            val photoUrl: String,
            val createdAt: String,
            val lat: Double?,
            val lon: Double?
        ): Parcelable
    }

    data class ResponseById(
        val error: Boolean,
        val message: String,
        val story: Data
    ) {
        data class Data(
            val id: String,
            val name: String,
            val description: String,
            val photoUrl: String,
            val createdAt: String,
            val lat: Float?,
            val long: Float?
        )
    }
}