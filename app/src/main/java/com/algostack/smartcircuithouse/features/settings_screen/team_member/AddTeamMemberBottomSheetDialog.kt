package com.algostack.smartcircuithouse.features.settings_screen.team_member
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.algostack.smartcircuithouse.R
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AddTeamMemberBottomSheetDialog : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_team_member_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser ? = firebaseAuth.currentUser

        // email password authentication using firebase
        val email = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val password = view.findViewById<TextInputEditText>(R.id.editTextPassword)
        val saveBtn = view.findViewById<MaterialCardView>(R.id.buttonsignupWithEmailPassword)
        val progress = view.findViewById<SpinKitView>(R.id.spin_kit)

        saveBtn.setOnClickListener {
            progress.visibility = View.VISIBLE
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if(emailText.isNotEmpty() && passwordText.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Toast with app icon

                                        Toast.makeText(context, "Team Member Added", Toast.LENGTH_SHORT).show()


                                        // clear the fields
                                        email.setText("")
                                        password.setText("")
                                        progress.visibility = View.GONE

                                        dismiss()
                                    }
                                }
                        }
                    }
            }
        }








    }
}