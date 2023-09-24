package com.example.aviator.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.aviator.R
import com.example.aviator.constant.APP_PREFERENCES
import com.example.aviator.constant.GAME
import com.example.aviator.constant.LAST_DAY
import com.example.aviator.constant.MY_CASH
import com.example.aviator.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGameBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        GAME = this
        navController = Navigation.findNavController(this,R.id.id_nav_host)

        //установка полноэкранного режима
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    //функция получения последнего дня,когда был получен денежный приз
    fun getLastDay():String{
        val preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getString(LAST_DAY,"")
        return preferences ?: ""
    }

    //функция обновления последнего дня
    fun setLastDay(day:String){
        val pref = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        pref.edit()
            .putString(LAST_DAY,day)
            .apply()
    }

    //функция получения своего счета
    fun getMyCash(): Int {
        return getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).getInt(MY_CASH, 0)
    }

    //функция добавления денег к счету
    fun addMyCash(cash:Int){
        val pref = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        pref.edit()
            .putInt(MY_CASH,getMyCash()+cash)
            .apply()
    }

    //функция уменьшения счета за счет ставки
    fun minusMyCash(cash:Int){
        val pref = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        pref.edit()
            .putInt(MY_CASH,getMyCash()-cash)
            .apply()
    }






}