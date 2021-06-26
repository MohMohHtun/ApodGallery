package com.example.mohmoh.apodgallery.model

import com.example.mohmoh.apodgallery.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class ApodApiService {

    @Inject
    lateinit var api: ApodApi

    init {
        DaggerApiComponent.create().inject(this)
    }


    fun getApods(dateApod: String): Single<List<Apod>>{
        return api.getApods(dateApod)
    }

    fun getApodByDate(dateApod: String): Single<Apod>{
        return  api.getApodByDate(dateApod)
    }
}