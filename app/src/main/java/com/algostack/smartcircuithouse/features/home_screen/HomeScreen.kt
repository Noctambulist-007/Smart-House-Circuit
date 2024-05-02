package com.algostack.smartcircuithouse.features.home_screen

import AddBuildingBottomSheetDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.algostack.smartcircuithouse.R
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.databinding.FragmentHomeScreenBinding
import com.algostack.smartcircuithouse.features.home_screen.adapter.TabPagerAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingViewModel
import com.google.android.material.snackbar.Snackbar

class HomeScreen : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuildingViewModel by viewModels()

    private var backPressedTime: Long = 0
    private var doubleTapToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddBuilding.setOnClickListener {
            val bottomSheet = AddBuildingBottomSheetDialog()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.settingsImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_settingsScreen)
        }

        val adapter = TabPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        viewModel.itemCreated.observe(viewLifecycleOwner) { itemCreated ->
            if (itemCreated) {
                showSnackbar(view, "Item created")
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isDoubleTap()) {
                    if (doubleTapToExit) {
                        requireActivity().finish()
                    } else {
                        doubleTapToExit = true
                        showSnackbar(view, "Press back again to exit")
                        Handler().postDelayed({ doubleTapToExit = false }, DOUBLE_TAP_INTERVAL)
                    }
                } else {
                    doubleTapToExit = false
                    requireActivity().onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun isDoubleTap(): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - backPressedTime
        backPressedTime = currentTime

        return timeDifference <= DOUBLE_TAP_INTERVAL
    }

    companion object {
        private const val DOUBLE_TAP_INTERVAL = 2000L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(view.context, R.color.primary))
            .setTextColor(ContextCompat.getColor(view.context, R.color.white))
            .show()
    }
}

