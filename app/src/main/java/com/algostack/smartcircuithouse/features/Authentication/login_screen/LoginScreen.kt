package com.algostack.smartcircuithouse.features.authentication.login_screen

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentLoginScreenBinding
import com.algostack.smartcircuithouse.utils.TokenManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : Fragment() {

    private val firebaseAuth = Firebase.auth
    private val firebaseUser = firebaseAuth.currentUser




    var _binding: FragmentLoginScreenBinding? = null
    val binding get() = _binding!!

    //private val loginViewModel : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        binding.icClose.setOnClickListener {
//            findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
//        }



        binding.buttonLoginWithEmailPassword.setOnClickListener {
            validateUserInput()
        }
    }


    private fun validateUserInput() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            binding.editTextEmail.error = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = "Enter a valid email"
        } else if (TextUtils.isEmpty(password)) {
            binding.editTextPassword.error = "Password is required"
        } else {

            binding.tvLogin.visibility = View.INVISIBLE
            binding.spinKit.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // save firebse UID in shared preferences and check if user is already logged in or not
                       TokenManager(requireContext()).saveUid(firebaseUser?.uid.toString())
                        binding.tvLogin.visibility = View.VISIBLE
                        binding.tvLogin.text = "Login successful"
                        binding.spinKit.visibility = View.INVISIBLE
                        findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Invalid email or password",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.tvLogin.visibility = View.VISIBLE
                        binding.spinKit.visibility = View.INVISIBLE
                    }
                }

        }
    }
}