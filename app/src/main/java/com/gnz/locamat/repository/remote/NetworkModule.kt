package com.gnz.locamat.repository.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


val networkModule = module {
    single { createRetrofit() }
}

const val BASE_URL = "http://207.154.210.145:8080/"

fun createRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(createOkHttp())
        .build()

private fun createOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .build()