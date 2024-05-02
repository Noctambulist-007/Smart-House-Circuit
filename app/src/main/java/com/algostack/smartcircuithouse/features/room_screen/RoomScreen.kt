package com.algostack.smartcircuithouse.features.room_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentRoomScreenBinding
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.AddRoomBottomSheetDialog
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.features.room_screen.model.RoomViewModel
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.android.material.progressindicator.LinearProgressIndicator

class RoomScreen : Fragment(), RoomAdapter.OnBookNowClickListener, RoomAdapter.OnDeleteClickListener {

    private var _binding: FragmentRoomScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
                val roomRepository = RoomRepository(roomDao)

                if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RoomViewModel(roomRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildingId = arguments?.getString("buildingId") ?: "-1"
        val buildingName = arguments?.getString("title") ?: "Default Building Name"
        val buildingPrimaryKey = arguments?.getString("primaryKey") ?: "Default Primary Key"

        val toolbar = view.findViewById<Toolbar>(R.id.toolbarBuildingName)
        toolbar.title = buildingName

        val adapter = RoomAdapter(this, this)
        adapter.setOnDeleteClickListener(this)
        binding.allRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val progressBar = view.findViewById<LinearProgressIndicator>(R.id.roomProgressBar)

        progressBar.visibility = View.VISIBLE

        viewModel.getRoomsForBuilding(buildingPrimaryKey).observe(viewLifecycleOwner) { rooms ->
            if (rooms.isEmpty()) {
                progressBar.visibility = View.GONE
                binding.textViewNoItems.visibility = View.VISIBLE
                binding.textViewTotalRoomItems.text = "Total Items: ${rooms.size}"

            } else {
                binding.textViewNoItems.visibility = View.GONE
                progressBar.visibility = View.GONE
                adapter.submitList(rooms)
                binding.textViewTotalRoomItems.text = "Total Items: ${rooms.size}"
            }
        }

        binding.fabAddRoom.setOnClickListener {
            val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
            val bottomSheet = AddRoomBottomSheetDialog(roomDao, buildingId, buildingName, buildingPrimaryKey)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    fun updateRoomStatus(roomData: RoomData) {
        val adapter = _binding?.allRecyclerView?.adapter as? RoomAdapter ?: return
        val position = adapter.currentList.indexOfFirst { it.id == roomData.id }
        if (position != -1) {
            adapter.updateRoomStatus(position)
        }
    }


    override fun onBookNowClick(roomData: RoomData) {
        val bottomSheet = BookingBottomSheetDialog()
        bottomSheet.setSelectedRoom(roomData)
        bottomSheet.setRoomScreen(this)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onCancelBookingClick(roomData: RoomData) {
        viewModel.cancelRoomBooking(roomData)
    }

    override fun onDeleteClick(roomData: RoomData) {
        viewModel.deleteRoom(roomData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
