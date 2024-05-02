package com.algostack.smartcircuithouse.features.home_screen.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.MainActivity
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.adapter.FilteredItemsAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModelFactory
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import com.algostack.smartcircuithouse.features.settings_screen.language_change.LanguageManager
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FilterByBottomSheetDialog : BottomSheetDialogFragment() {

    private var isCheckInSelected: Boolean? = null

    interface FilterListener {
        fun onFilterSelected(isCheckIn: Boolean)
    }

    private var filterListener: FilterListener? = null

    fun setFilterListener(listener: FilterListener) {
        filterListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filter_by_in_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val checkInRadioButton = view.findViewById<RadioButton>(R.id.checkInRadioButton)
        val checkOutRadioButton = view.findViewById<RadioButton>(R.id.checkOutRadioButton)

        checkInRadioButton.setOnClickListener {
            isCheckInSelected = true
            filterListener?.onFilterSelected(true)
            dismiss()
        }

        checkOutRadioButton.setOnClickListener {
            isCheckInSelected = false
            filterListener?.onFilterSelected(false)
            dismiss()
        }


    }


}