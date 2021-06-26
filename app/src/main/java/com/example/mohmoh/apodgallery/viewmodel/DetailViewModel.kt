package com.example.mohmoh.apodgallery.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mohmoh.apodgallery.model.Apod
import com.example.mohmoh.apodgallery.model.ApodDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    val apodLiveData= MutableLiveData<Apod>()

    fun fetch(date_arg: String) {

        launch {
            val apod = ApodDatabase(getApplication()).apodDao().getApod(date_arg)
            apodLiveData.value = apod
        }


    }
}