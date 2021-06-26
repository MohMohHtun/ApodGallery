package com.example.mohmoh.apodgallery.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.mohmoh.apodgallery.di.DaggerApiComponent
import com.example.mohmoh.apodgallery.model.Apod
import com.example.mohmoh.apodgallery.model.ApodApiService
import com.example.mohmoh.apodgallery.model.ApodDatabase
import com.example.mohmoh.apodgallery.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())

    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L


    @Inject
    lateinit var apodApiService : ApodApiService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val disposable = CompositeDisposable()

    val apods = MutableLiveData<List<Apod>>()
    val apodLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh(startDate: String){
        val updateTime = prefHelper.getUpdateTime()

        if ((updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) || !isOnline(getApplication())){
            fetchFromDatabase()
        }else{
            fetchApods((startDate))
        }

    }
    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val apods = ApodDatabase(getApplication()).apodDao().getAllAPODs()
            apodRetrieved(apods)

            var text = "Data Retrieved from Database"
            if (!isOnline(getApplication())){
                text = "No Connection! " + text
            }
            Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show()
        }
    }

     fun fetchApod(date_Apod: String){
         if (!isOnline(getApplication())){
             fetchFromDatabase()
             return
         }
        loading.value = true
        disposable.add(
            apodApiService.getApodByDate(date_Apod)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Apod>() {
                    override fun onSuccess(t: Apod) {

                        storeAndAddApodLocally(t)


                    }

                    override fun onError(e: Throwable) {
                        apodLoadError.value = true
                        loading.value = false


                    }
                })
        )
    }


    fun fetchApods(startDate: String){
        if (!isOnline(getApplication())){
            fetchFromDatabase()
            return
        }
        loading.value = true
        disposable.add(
            apodApiService.getApods(startDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Apod>>() {
                    override fun onSuccess(t: List<Apod>) {

                        storeApodLocally(t)

                    }

                    override fun onError(e: Throwable) {
                        apodLoadError.value = true
                        loading.value = false


                    }
                })
        )
    }

    private fun apodRetrieved(apodList: List<Apod>){
        apods.value = apodList
        apodLoadError.value = false
        loading.value = false
    }
    private fun apodUpdate(apodList: List<Apod>){

        var tmp: List<Apod>  = apods.value ?: mutableListOf()


        var secTmp = apodList.plus(tmp)
        apods.value = secTmp


        apodLoadError.value = false
        loading.value = false

    }

    private fun storeApodLocally(list: List<Apod>){
        launch {

            val dao = ApodDatabase(getApplication()).apodDao()
            dao.deleteAllApods()
            val result = dao.insertAll(*list.toTypedArray())


            apodRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun storeAndAddApodLocally(apod: Apod){

        launch {

            val dao = ApodDatabase(getApplication()).apodDao()
            //dao.deleteAllApods()
            val result = dao.insertAll(*listOf<Apod>(apod).toTypedArray())


            apodUpdate(listOf<Apod>(apod))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

//    fun isOnline(context: Context): Boolean {
//        val connectivityManager =
//                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val capabilities =
//                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    return true
//                }
//            }
//        }
//        return false
//    }

    fun isOnline(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}