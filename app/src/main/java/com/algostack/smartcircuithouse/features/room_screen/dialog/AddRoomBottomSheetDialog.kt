package com.algostack.smartcircuithouse.features.room_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.services.db.RoomDao
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddRoomBottomSheetDialog(private val roomDao: RoomDao, private val buildingId: String, private val buildingName: String, private val buildingPrimaryKey: String) :

    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_room_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etRoomNo = view.findViewById<TextInputEditText>(R.id.editTextRoomNo)
        val spinnerBedType: Spinner = view.findViewById(R.id.spinnerBedType)
        val etFloorNo = view.findViewById<TextInputEditText>(R.id.editTextFloorNo)
        val btnSave = view.findViewById<Button>(R.id.roomBtnSave)
        val bedTypesArray = resources.getStringArray(R.array.bed_types).toMutableList()

        bedTypesArray.add(0, getString(R.string.bed_type_hint))

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            bedTypesArray
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerBedType.adapter = adapter

        btnSave.setOnClickListener {
            val roomNo = etRoomNo.text.toString().trim()
            val bedType = spinnerBedType.selectedItem.toString().trim()
            val floorNo = etFloorNo.text.toString().trim()

            if (roomNo.isNotEmpty() && bedType != getString(R.string.bed_type_hint) && floorNo.isNotEmpty()) {
                val roomData = RoomData(
                    roomNo = roomNo,
                    bedType = bedType,
                    floorNo = floorNo,
                    buildingId = buildingId,
                    roomBuildingName = buildingName,
                    primaryKey = buildingPrimaryKey

                )
                saveRoomInfo(roomData)
                dismiss()
            } else {
                if (roomNo.isEmpty()) {
                    etRoomNo.error = "Room number is required"
                }
                if (bedType == getString(R.string.bed_type_hint)) {
                    Toast.makeText(requireContext(), "Please select a bed type", Toast.LENGTH_SHORT)
                        .show()
                }
                if (floorNo.isEmpty()) {
                    etFloorNo.error = "Floor number is required"
                }
            }
        }
    }

    private fun saveRoomInfo(roomData: RoomData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingRoom = roomDao.getRoomByNumberAndFloorAndBuilding(
                    roomData.roomNo,
                    roomData.floorNo,
                    roomData.roomBuildingName
                )
                if (existingRoom == null) {
                    roomDao.insert(roomData)
                } else {
                    showToast("Room with the same details already exists")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
