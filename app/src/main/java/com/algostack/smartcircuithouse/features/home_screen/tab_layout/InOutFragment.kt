package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.adapter.FilteredItemsAdapter
import com.algostack.smartcircuithouse.features.home_screen.adapter.InOutAdapter
import com.algostack.smartcircuithouse.features.home_screen.dialog.FilterByBottomSheetDialog
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModelFactory
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import com.algostack.smartcircuithouse.features.settings_screen.language_change.LanguageChangeBottomSheetDialog
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import kotlinx.coroutines.*

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InOutFragment : Fragment(), FilterByBottomSheetDialog.FilterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InOutAdapter
    private val viewModel: InOutViewModel by viewModels {
        val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
        val roomRepository = RoomRepository(roomDao)
        InOutViewModelFactory(roomRepository, this)
    }

    private var progressDialog: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_in_out, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewInOut)
        adapter = InOutAdapter(viewModel)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@InOutFragment.adapter
        }

        val imageViewFilterDate = view.findViewById<ImageView>(R.id.imageViewFilterDate)

        imageViewFilterDate.setOnClickListener {
            val bottomSheet = FilterByBottomSheetDialog()
            bottomSheet.setFilterListener(this)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }


        val textViewTotalInOutItems = view.findViewById<TextView>(R.id.textViewTotalInOutItems)
        val textViewNoItems = view.findViewById<TextView>(R.id.textViewNoItems)

        viewModel.itemList.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.isEmpty()) {
                textViewNoItems.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                textViewTotalInOutItems.text = "Total Items: ${itemList.size}"
            } else {
                textViewNoItems.visibility = View.GONE
                adapter.submitList(itemList)
                recyclerView.visibility = View.VISIBLE
                textViewTotalInOutItems.text = "Total Items: ${itemList.size}"
            }
        })

        return view
    }

    private fun showDatePicker(isCheckIn: Boolean) {
        val datePicker = DatePickerDialog(requireContext(), R.style.DatePickerDialogStyle)
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedCalendar.time)

            if (isCheckIn) {
                filterItemsByEntryDate(formattedDate)
            } else {
                filterItemsByExitDate(formattedDate)
            }
        }

        datePicker.show()
    }

    private fun filterItemsByEntryDate(formattedDate: String) {
        showLoadingDialog()
        viewModel.filterItemsByEntryDate(formattedDate) { filteredItems ->
            dismissLoadingDialog()
            if (filteredItems.isNotEmpty()) {
                Log.d("Filtering", "Filtering successful")
                showFilteredItemsDialog(filteredItems)
            } else {
                Log.e("Filtering", "No items found for the selected entry date")
                AlertDialog.Builder(requireContext())
                    .setTitle("No Items Found")
                    .setMessage("No items found for the selected entry date.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }


    private fun filterItemsByExitDate(formattedDate: String) {
        showLoadingDialog()
        viewModel.filterItemsByExitDate(formattedDate) { filteredItems ->
            dismissLoadingDialog()
            if (filteredItems.isNotEmpty()) {
                Log.d("Filtering", "Filtering successful")
                showFilteredItemsDialog(filteredItems)
            } else {
                Log.e("Filtering", "No items found for the selected exit date")
                AlertDialog.Builder(requireContext())
                    .setTitle("No Items Found")
                    .setMessage("No items found for the selected exit date.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }


    private fun showFilteredItemsDialog(filteredItems: List<Item>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filtered_items, null)
        val recyclerViewFilteredItems =
            dialogView.findViewById<RecyclerView>(R.id.recyclerViewFilteredItems)
        recyclerViewFilteredItems.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFilteredItems.adapter = FilteredItemsAdapter(filteredItems)

        val dialogTitle = "Filtered Items (${filteredItems.size} Found)"
        AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoadingDialog() {
        progressDialog = AlertDialog.Builder(requireContext())
            .setTitle("Filtering Items")
            .setMessage("Please wait...")
            .setCancelable(false)
            .show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.dismiss()
    }

    override fun onFilterSelected(isCheckIn: Boolean) {
        if (isCheckIn) {
            showDatePicker(isCheckIn = true)
        } else {
            showDatePicker(isCheckIn = false)
        }
    }
}

