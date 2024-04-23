package com.example.aviator.view.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.aviator.constant.APP_PREFERENCES
import com.example.aviator.constant.ID
import com.example.aviator.constant.image_url_air
import com.example.aviator.constant.image_url_splash
import com.example.aviator.databinding.ActivitySplashBinding
import com.example.aviator.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var binding : ActivitySplashBinding? = null
    private var job : Job = Job()
    private var job2 : Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        //загрузка фоновой картинки
        loadBackgroundImage(image_url_splash,binding?.idSplashImg)

        //загрузка картинки эмблемы игры
        loadImage(image_url_air,binding?.idSplashIvAir)

        //создание и старт анимации
        startAnimation()

        val splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        val namePhone = Build.MODEL.toString()
        val locale = Locale.getDefault().displayLanguage.toString()
        val id: String

        if (getMyId()!=""){
            id = getMyId()
        }else{
            id = UUID.randomUUID().toString()
            setMyId(id)
        }

        splashViewModel.setPostParametersPhone(namePhone,locale,id)

        splashViewModel.webViewUrl.observe(this){ responce ->
            when(responce.body()!!.url){
                "no" -> { goToMainPush() }
                "nopush" -> { goToMainNoPush() }
                else -> { goToWeb(responce.body()!!.url) }
            }
        }

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            startActivity(Intent(this@SplashActivity,GameActivity::class.java))
        }

    }

    //прекращение загрузки и выход из игры при нажатии на кнопку НАЗАД
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if(job.isActive){
            job.cancel()
        }
        if(job2.isActive){
            job2.cancel()
        }
        finishAffinity()
    }

    //функция получения ID
    private fun getMyId() : String{
        return getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(ID,"").toString()
    }

    //функция установки ID
    private fun setMyId(id:String){
        getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().putString(ID,id).apply()
    }

    //функция загрузки изображения с сервера
    private fun loadBackgroundImage(url:String,id:ImageView?){
        id?.let {
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(it)
        }
    }

    //функция загрузки изображения с сервера
    private fun loadImage(url:String,id:ImageView?){
        id?.let {
            Glide.with(this)
                .load(url)
                .into(it)
        }
    }

    //функция создания и старта анимации загрузки
    private fun startAnimation(){
        binding?.idSplashProgressBar?.max = 2000
        val finishProgress = 2000
        ObjectAnimator.ofInt(binding?.idSplashProgressBar,"progress",finishProgress)
            .setDuration(4000)
            .start()
    }


    private fun goToMainPush() {
        job2 = CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            startActivity(Intent(this@SplashActivity,GameActivity::class.java))
        }
    }

    private fun goToMainNoPush() {
        job2 = CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            startActivity(Intent(this@SplashActivity,GameActivity::class.java))
        }
    }

    private fun goToWeb(url:String) {
        job2 = CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            val intent = Intent(this@SplashActivity,WebViewActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }

}