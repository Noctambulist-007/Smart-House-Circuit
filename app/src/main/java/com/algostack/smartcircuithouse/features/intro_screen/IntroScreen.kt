package com.algostack.smartcircuithouse.features.intro_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentIntroScreenBinding

class IntroScreen : Fragment() {

    var _binding: FragmentIntroScreenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIntroScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonIntroLetsGo.setOnClickListener {
            findNavController().navigate(R.id.action_introScreen_to_loginScreen)
        }
    }

}