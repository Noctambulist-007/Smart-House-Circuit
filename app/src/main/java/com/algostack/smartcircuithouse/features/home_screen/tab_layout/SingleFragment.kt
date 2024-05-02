package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.databinding.FragmentSingleBinding
import com.algostack.smartcircuithouse.features.home_screen.model.SingleViewModelFactory
import com.algostack.smartcircuithouse.features.home_screen.model.SingleViewModel
import com.algostack.smartcircuithouse.features.room_screen.RoomScreen
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.services.model.RoomData

class SingleFragment : Fragment(), RoomAdapter.OnBookNowClickListener {

    private val viewModel: SingleViewModel by viewModels { SingleViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentSingleBinding
    private lateinit var roomScreen: RoomScreen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = RoomScreen()

        roomScreen = activity

        val adapter = RoomAdapter(this, null)
        binding.recyclerViewSingleRooms.adapter = adapter
        binding.recyclerViewSingleRooms.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getSingleRooms().observe(viewLifecycleOwner, Observer { rooms ->
            if (rooms.isEmpty()) {
                binding.textViewNoItems.visibility = View.VISIBLE
                binding.recyclerViewSingleRooms.visibility = View.GONE
                binding.textViewTotalSingleItems.text = "Total Items: ${rooms.size}"
            } else {
                binding.textViewNoItems.visibility = View.GONE
                binding.recyclerViewSingleRooms.visibility = View.VISIBLE
                adapter.submitList(rooms)
                binding.textViewTotalSingleItems.text = "Total Items: ${rooms.size}"
            }
        })
    }

    override fun onBookNowClick(roomData: RoomData) {
        /*viewModel.bookRoom(roomData)*/
        val bookingBottomSheetDialog = BookingBottomSheetDialog()
        bookingBottomSheetDialog.setSelectedRoom(roomData)
        bookingBottomSheetDialog.setRoomScreen(roomScreen)
        bookingBottomSheetDialog.show(parentFragmentManager, "BookingBottomSheetDialog")
    }

    override fun onCancelBookingClick(roomData: RoomData) {
        viewModel.cancelRoomBooking(roomData)
    }
}
