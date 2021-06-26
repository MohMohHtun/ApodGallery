package com.example.mohmoh.apodgallery.di

import com.example.mohmoh.apodgallery.model.ApodApiService
import com.example.mohmoh.apodgallery.viewmodel.ListViewModel
import dagger.Component


@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: ApodApiService)

    fun inject(viewModel: ListViewModel)
}