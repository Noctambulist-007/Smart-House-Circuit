package com.algostack.smartcircuithouse.features.settings_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentSettingsScreenBinding
import com.algostack.smartcircuithouse.features.settings_screen.model.SettingViewModelFactory
import com.algostack.smartcircuithouse.features.settings_screen.language_change.LanguageChangeBottomSheetDialog
import com.algostack.smartcircuithouse.features.settings_screen.team_member.AddTeamMemberBottomSheetDialog
import com.algostack.smartcircuithouse.features.settings_screen.viewmodel.SettingViewModel
import com.algostack.smartcircuithouse.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsScreen : Fragment() {

    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    private val settingViewModel by viewModels<SettingViewModel> {
        SettingViewModelFactory(
            requireContext()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        if(TokenManager(requireContext()).getUid() != null){
            if (currentUser != null) {
                //get current login email
                val email = currentUser.email
                binding.demoMail.text = email
            }
        }




        binding.backupData.setOnClickListener {
            // impliment bcakaground task to backup data

            CoroutineScope(Dispatchers.Main).launch {
                settingViewModel.getAllDataForBackup(requireContext())
            }

        }


        binding.SyncData.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                settingViewModel.syncData(requireContext())
            }
        }


        binding.changeLanguage.setOnClickListener {

            val bottomSheet = LanguageChangeBottomSheetDialog()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }


        binding.addteammember.setOnClickListener {
            val bottomSheet = AddTeamMemberBottomSheetDialog()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.LogOutbtn.setOnClickListener {
            firebaseAuth.signOut()
            TokenManager(requireContext()).clearUid()
            findNavController().navigate(R.id.action_settingsScreen_to_loginScreen)

        }

        binding.layoutAbout.setOnClickListener {
            showAboutDialog()
        }

    }

    private fun showAboutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_about, null)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok, null)
            .create()

        alertDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
