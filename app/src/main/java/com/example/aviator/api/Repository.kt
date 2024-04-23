package com.example.aviator.api

import com.example.aviator.model.ResponseWebView
import retrofit2.Response

class Repository {

    suspend fun setParametersPhone(phone_name : String , locale : String , unique : String) : Response<ResponseWebView> {
        return RetrofitInstance.api.setPostParametersPhone(phone_name , locale , unique)
    }

}