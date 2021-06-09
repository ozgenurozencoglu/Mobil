package com.ozgenur.odev.ui.vm.main
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.ozgenur.odev.data.model.User
import com.ozgenur.odev.data.repository.MainRepository
import com.ozgenur.odev.utils.Resource
import com.ozgenur.odev.utils.UserApp

import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(app: Application,
                    private val mainRepository: MainRepository
): AndroidViewModel(app) {

    // Resource
    val userRes: MutableLiveData<Resource<MutableList<User>>> = MutableLiveData()
    // Response
    var userResponse: MutableList<User>? = null

    fun getUser() = viewModelScope.launch {
        safeGetUserCall()
    }

    private suspend fun safeGetUserCall() {
        userRes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getUsers()
                Log.i("USER", response.toString())
                userRes.postValue(handleGetUserResponse(response))
            } else {
                userRes.postValue(Resource.Error("Lütfen internet bağlantınızı kontrol ediniz."))
            }
        } catch (e: Exception) {
            userRes.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun handleGetUserResponse(response: Response<MutableList<User>>): Resource<MutableList<User>> {
        if (response.isSuccessful) {
            response.body()?.let { users ->
                return Resource.Success(userResponse ?: users)
            }
        }
        return Resource.Error("Bilinmeyen bir hata oluştu:\n" + response.code().toString())
    }

/*
    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, exception.message ?: "Bir hata oluştu..."))
        }
    }
  */

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<UserApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}