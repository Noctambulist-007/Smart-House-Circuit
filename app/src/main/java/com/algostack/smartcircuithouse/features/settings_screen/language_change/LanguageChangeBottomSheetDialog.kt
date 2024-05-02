package com.algostack.smartcircuithouse.features.settings_screen.language_change

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.algostack.smartcircuithouse.MainActivity
import com.algostack.smartcircuithouse.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageChangeBottomSheetDialog  : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.language_change, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val eng = view.findViewById<RadioButton>(R.id.englishRadioButton)
        val bng = view.findViewById<RadioButton>(R.id.banglaRadioButton)

        bng.setOnClickListener {
            val languageManager = LanguageManager(requireContext())
            languageManager.updateLanguage(requireContext(),"bn")
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }

        eng.setOnClickListener {
            val languageManager = LanguageManager(requireContext())
            languageManager.updateLanguage(requireContext(),"en")
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }


    }
}