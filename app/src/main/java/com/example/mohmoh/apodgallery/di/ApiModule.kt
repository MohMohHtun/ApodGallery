package com.example.mohmoh.apodgallery.di

import com.example.mohmoh.apodgallery.model.ApodApi
import com.example.mohmoh.apodgallery.model.ApodApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class ApiModule {

    private val BASE_URL = "https://api.nasa.gov"

    @Provides
    fun provideApodApi(): ApodApi {
          return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApodApi::class.java)
    }

    @Provides
    fun provideApodApiService() : ApodApiService {
        return ApodApiService()
    }
}