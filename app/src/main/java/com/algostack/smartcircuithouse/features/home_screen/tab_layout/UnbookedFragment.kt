package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.databinding.FragmentUnbookedBinding
import com.algostack.smartcircuithouse.features.home_screen.model.UnbookedViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.UnbookedViewModelFactory
import com.algostack.smartcircuithouse.features.room_screen.RoomScreen
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData

class UnbookedFragment : Fragment(), RoomAdapter.OnBookNowClickListener {

    private val viewModel: UnbookedViewModel by viewModels { UnbookedViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentUnbookedBinding
    private lateinit var roomScreen: RoomScreen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnbookedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = RoomScreen()

        roomScreen = activity

        val adapter = RoomAdapter(this, null)
        binding.recyclerViewUnbookedRooms.adapter = adapter
        binding.recyclerViewUnbookedRooms.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getUnbookedRooms().observe(viewLifecycleOwner, Observer { rooms ->
            if (rooms.isEmpty()) {
                binding.textViewNoItems.visibility = View.VISIBLE
                binding.recyclerViewUnbookedRooms.visibility = View.GONE
                binding.textViewTotalUnbookedItems.text = "Total Items: ${rooms.size}"
            } else {
                binding.textViewNoItems.visibility = View.GONE
                binding.recyclerViewUnbookedRooms.visibility = View.VISIBLE
                adapter.submitList(rooms)
                binding.textViewTotalUnbookedItems.text = "Total Items: ${rooms.size}"
            }

        })
    }

    override fun onBookNowClick(roomData: RoomData) {
        val bookingBottomSheetDialog = BookingBottomSheetDialog()
        bookingBottomSheetDialog.setSelectedRoom(roomData)
        bookingBottomSheetDialog.setRoomScreen(roomScreen)
        bookingBottomSheetDialog.show(parentFragmentManager, "BookingBottomSheetDialog")
    }

    override fun onCancelBookingClick(roomData: RoomData) {
        viewModel.cancelRoomBooking(roomData)
    }
}
