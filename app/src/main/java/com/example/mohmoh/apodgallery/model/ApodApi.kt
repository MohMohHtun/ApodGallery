package com.example.mohmoh.apodgallery.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApi {

    @GET("planetary/apod?api_key=NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo&")
    fun getApods(@Query("start_date") dateApod: String): Single<List<Apod>>

    @GET("planetary/apod?api_key=NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo&")
    fun getApodByDate(@Query("date") dateApod: String): Single<Apod>
}