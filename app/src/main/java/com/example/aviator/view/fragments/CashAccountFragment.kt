package com.example.aviator.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import com.bumptech.glide.Glide
import com.example.aviator.R
import com.example.aviator.constant.GAME
import com.example.aviator.constant.TIME_LONG
import com.example.aviator.constant.TIME_SHORT
import com.example.aviator.constant.image_url_air
import com.example.aviator.databinding.FragmentCashAccountBinding
import org.threeten.bp.LocalDate

class CashAccountFragment : Fragment() {

    private var binding:FragmentCashAccountBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCashAccountBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //загрузка эмблемы игры
        loadImage(image_url_air,binding!!.idCashAccountIvAir)

        //проверка прошел ли день после пополнения счета
        if(checkLastDay()){
            showToast("you have the opportunity to top up your account", TIME_LONG)
        }else{
            showToast("you will be able to top up your account only the next day", TIME_LONG)
        }

        //получение денег
        binding!!.idCashAccountButtonAddMoney.setOnClickListener {
            if(checkLastDay()){
                //true
                val cashWin = listOf(100,200,300,400,500,600,700,800,900,1000).shuffled()[0]
                showToast("$cashWin$", TIME_SHORT)
                binding!!.idCashAccountTvYourMoney.text = "your money:$cashWin$"
                GAME.setLastDay(LocalDate.now().toString())
                GAME.addMyCash(cashWin)
            }else{
                //false
                showToast("you will be able to top up your account only the next day", TIME_LONG)
            }
        }

        //переход от пополнения счета к игре
        binding!!.idCashAccountButtonBack.setOnClickListener {
            GAME.navController?.navigate(R.id.action_cashAccountFragment_to_gameFragment)
        }

        //переход от пополнения счета к игре
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            GAME.navController?.navigate(R.id.action_cashAccountFragment_to_gameFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //функция загрузки изображения с сервера
    private fun loadImage(url:String,id: ImageView){
        Glide.with(this)
            .load(url)
            .into(id)
    }

    private fun showToast(message:String,time:String){
        when(time){
            TIME_SHORT -> { Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show() }
            TIME_LONG -> { Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show() }
        }
    }

    private fun checkLastDay() : Boolean  = LocalDate.now().toString()!= GAME.getLastDay()


}