package com.algostack.smartcircuithouse.features.room_screen.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.room_screen.RoomScreen
import com.algostack.smartcircuithouse.features.room_screen.model.RoomViewModel
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: RoomViewModel by lazy {
        val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
        val roomRepository = RoomRepository(roomDao)

        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RoomViewModel(roomRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }).get(RoomViewModel::class.java)
    }

    private lateinit var selectedRoom: RoomData
    private lateinit var roomScreen: RoomScreen

    private lateinit var customerNameEditText: TextInputEditText
    private lateinit var customerDetailsEditText: TextInputEditText
    private lateinit var enterDateEditText: TextInputEditText
    private lateinit var exitDateEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_book_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBookNow = view.findViewById<Button>(R.id.buttonBookNow)
        buttonBookNow.setOnClickListener {
            handleRoomBooking()
        }

        customerNameEditText = view.findViewById(R.id.editTextCustomerName)
        customerDetailsEditText = view.findViewById(R.id.editTextCustomerDetails)
        enterDateEditText = view.findViewById(R.id.editTextEnterDate)
        exitDateEditText = view.findViewById(R.id.editTextExitDate)

        enterDateEditText.setOnClickListener { showDatePicker(enterDateEditText) }
        exitDateEditText.setOnClickListener { showDatePicker(exitDateEditText) }
    }

    private fun handleRoomBooking() {

        if (!::roomScreen.isInitialized) {
            Toast.makeText(requireContext(), "roomScreen is not initialized", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val customerName = customerNameEditText.text?.toString()
        val customerDetails = customerDetailsEditText.text?.toString()
        val enterDate = enterDateEditText.text?.toString()
        val exitDate = exitDateEditText.text?.toString()

        if (customerName.isNullOrEmpty()) {
            customerNameEditText.error = "Customer name is required"
            return
        }

        if (customerDetails.isNullOrEmpty()) {
            customerDetailsEditText.error = "Customer details are required"
            return
        }

        if (enterDate.isNullOrEmpty()) {
            enterDateEditText.error = "Enter date is required"
            Toast.makeText(requireContext(), "Enter date is required", Toast.LENGTH_SHORT).show()
            return
        }

        if (exitDate.isNullOrEmpty()) {
            exitDateEditText.error = "Exit date is required"
            Toast.makeText(requireContext(), "Exit date is required", Toast.LENGTH_SHORT).show()
            return
        }


        selectedRoom.isBooked = true
        selectedRoom.customerName = customerName
        selectedRoom.customerDetails = customerDetails

        val buildingId = selectedRoom.buildingId
        val buildingName = selectedRoom.roomBuildingName
        val floorNumber = selectedRoom.floorNo
        val roomNumber = selectedRoom.roomNo
        val bedType = selectedRoom.bedType

        val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
        val entryDate = dateFormat.parse(enterDate)
        val exitDateParsed = dateFormat.parse(exitDate)

        if (exitDateParsed?.before(entryDate) == true) {
            exitDateEditText.error = "Exit date must be greater than enter date"
            return
        }

        selectedRoom.entryDate = entryDate?.time
        selectedRoom.exitDate = exitDateParsed?.time

        viewModel.bookRoom(selectedRoom, buildingId, buildingName, floorNumber, roomNumber, bedType)

        roomScreen.updateRoomStatus(selectedRoom)

        val message = "Room booked successfully"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        dismiss()
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialogStyle,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)

                editText.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    fun setSelectedRoom(room: RoomData) {
        selectedRoom = room
    }

    fun setRoomScreen(roomScreen: RoomScreen) {
        this.roomScreen = roomScreen
    }
}
