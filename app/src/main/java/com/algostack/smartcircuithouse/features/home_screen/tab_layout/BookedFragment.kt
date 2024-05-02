package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.databinding.FragmentBookedBinding
import com.algostack.smartcircuithouse.features.home_screen.model.BookedViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.BookedViewModelFactory
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.services.model.RoomData

class BookedFragment : Fragment(), RoomAdapter.OnBookNowClickListener {

    private val viewModel: BookedViewModel by viewModels { BookedViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentBookedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoomAdapter(this, null)

        binding.recyclerViewBookedRooms.adapter = adapter
        binding.recyclerViewBookedRooms.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getBookedRooms().observe(viewLifecycleOwner, Observer { rooms ->
            adapter.submitList(rooms)
            binding.textViewTotalBookedItems.text = "Total Items: ${rooms.size}"
        })
    }

    override fun onBookNowClick(roomData: RoomData) {
        val bookingBottomSheetDialog = BookingBottomSheetDialog()
        bookingBottomSheetDialog.setSelectedRoom(roomData)
        bookingBottomSheetDialog.show(parentFragmentManager, "BookingBottomSheetDialog")
    }

    override fun onCancelBookingClick(roomData: RoomData) {
        viewModel.cancelRoomBooking(roomData)
    }
}
