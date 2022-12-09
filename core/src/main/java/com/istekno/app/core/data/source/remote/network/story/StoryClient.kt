package com.istekno.app.core.data.source.remote.network.story

import com.istekno.app.core.BuildConfig.BASE_URL
import com.istekno.app.core.data.source.remote.network.OkHttpClientInstance
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class StoryClient {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val okHttpClient = OkHttpClientInstance.Builder().buildToken()

    private fun apiClient(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val retrofitClient: StoryService by lazy {
        apiClient().create(StoryService::class.java)
    }
}