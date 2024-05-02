package com.algostack.smartcircuithouse.features.splash_screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentSplashScreenBinding
import com.algostack.smartcircuithouse.utils.TokenManager

class SplashScreen : Fragment() {

    var _binding: FragmentSplashScreenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        Handler(Looper.getMainLooper()).postDelayed({

            if (TokenManager(requireContext()).getUid() != null){
                  findNavController().navigate(R.id.action_splashScreen_to_homeScreen)
            }else{
                findNavController().navigate(R.id.action_splashScreen_to_introScreen)
            }




        }, 1500)
        return binding.root
    }


}