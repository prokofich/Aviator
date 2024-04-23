package com.example.aviator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aviator.api.Repository
import com.example.aviator.model.ResponseWebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SplashViewModel : ViewModel() {

    private val repository = Repository()
    val webViewUrl: MutableLiveData <Response <ResponseWebView> > = MutableLiveData()

    fun setPostParametersPhone(phone_name:String,locale:String,unique:String){
        viewModelScope.launch(Dispatchers.IO) {
            val responce = repository.setParametersPhone(phone_name, locale, unique)
            withContext(Dispatchers.Main){
                webViewUrl.value = responce
            }
        }
    }

}