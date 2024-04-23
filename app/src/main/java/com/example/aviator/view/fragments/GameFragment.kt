package com.example.aviator.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.example.aviator.R
import com.example.aviator.constant.GAME
import com.example.aviator.constant.listCoefficientStop
import com.example.aviator.databinding.FragmentGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class GameFragment : Fragment() {

    private var binding : FragmentGameBinding? = null
    private var jobCoef : Job = Job()
    private var yourCashForBet = 0
    private var coefficient = 1.0
    private var coefficientStop = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //показ своего счета
        binding!!.idGameTvYourMoney.text = "your money:${GAME.getMyCash()}$"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobCoef.cancel()

        //УВЕЛИЧЕНИЕ СТАВОК ЧЕРЕЗ + И - //////////////////////////////////////////////
        //прибавить к ставке 25$
        binding!!.idGameCsBetButtonPlus.setOnClickListener {
            if(!jobCoef.isActive){
                if(checkCash(yourCashForBet+25)){
                    yourCashForBet += 25
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }else{
                    showToast("not enough funds")
                }
            }
        }

        //уменьшить ставку на 25$
        binding!!.idGameCsBetButtonMinus.setOnClickListener {
            if(!jobCoef.isActive){
                if(yourCashForBet>=25){
                    yourCashForBet -= 25
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////

        //СТАВКИ В 50,100,200,500 //////////////////////////////////////////////////
        //сделать ставку в 200$
        binding!!.idGameCsBetButton200.setOnClickListener {
            if(!jobCoef.isActive){
                if(checkCash(200)){
                    yourCashForBet = 200
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }else{
                    showToast("not enough funds")
                }
            }
        }

        //сделать ставку в 100$
        binding!!.idGameCsBetButton100.setOnClickListener {
            if(!jobCoef.isActive){
                if(checkCash(100)){
                    yourCashForBet = 100
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }else{
                    showToast("not enough funds")
                }
            }
        }

        //сделать ставку в 50$
        binding!!.idGameCsBetButton50.setOnClickListener {
            if(!jobCoef.isActive){
                if(checkCash(50)){
                    yourCashForBet = 50
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }else{
                    showToast("not enough funds")
                }
            }
        }

        //сделать ставку в 500$
        binding!!.idGameCsBetButton500.setOnClickListener {
            if(!jobCoef.isActive){
                if(checkCash(500)){
                    yourCashForBet = 500
                    binding!!.idGameCsBetTvCashBet.text = "$yourCashForBet$"
                    binding!!.idGameTvCashWinning.text = "$yourCashForBet$"
                }else{
                    showToast("not enough funds")
                }
            }
        }

        /////////////////////////////////////////////////////////////////////////

        //принятие ставки и начало игры + возможность забрать выигрыш
        binding!!.idGameCsBetButtonBet.setOnClickListener {
            if(jobCoef.isActive){
                //получить выигрыш
                binding!!.idGameCsBetButtonBet.text = "GO"
                stopCoefficient()
                showToast("you won ${(coefficient*yourCashForBet).toInt()}$")
                GAME.addMyCash((coefficient*yourCashForBet).toInt())
                binding!!.idGameTvYourMoney.text = "your money:${GAME.getMyCash()}$"
                yourCashForBet = 0
                coefficient = 1.0
            }else{
                GAME.minusMyCash(yourCashForBet)
                binding!!.idGameTvYourMoney.text = "your money:${GAME.getMyCash()}$"
                binding!!.idGameCsBetButtonBet.text = "BET"
                setRandomCoefficientStop()
                startAddCoefficientForBet()
            }
        }

        //переход к пополнению счета
        binding!!.idGameButtonAddYourMoney.setOnClickListener {
            GAME.navController?.navigate(R.id.action_gameFragment_to_cashAccountFragment)
        }

        //выход из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            GAME.finishAffinity()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showToast(message:String) = Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()

    @SuppressLint("SetTextI18n")
    private fun startAddCoefficientForBet(){
        jobCoef = CoroutineScope(Dispatchers.Main).launch {
            while(coefficient<=coefficientStop){
                coefficient += 0.05
                coefficient = "%.2f".format(Locale.US,coefficient).toDouble()
                binding!!.idGameTvCoef.text = "$coefficient x"
                binding!!.idGameTvCashWinning.text = "winning ${(yourCashForBet.toDouble()*coefficient).toInt()}$"
                delay(500)
            }
            showToast("you've lost")
            binding!!.idGameTvCashWinning.text = "winning 0$"
            yourCashForBet = 0
            jobCoef.cancel()
        }
    }

    private fun checkCash(cash:Int): Boolean = GAME.getMyCash() >= cash


    //установка наибольшего значения коэффициента
    private fun setRandomCoefficientStop(){
        coefficientStop = listCoefficientStop.shuffled()[0]
    }

    private fun stopCoefficient(){
        if(jobCoef.isActive){
            jobCoef.cancel()
        }
    }


}