package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import java.util.*

class InOutAdapter(private val viewModel: InOutViewModel) :
    ListAdapter<Item, InOutAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private val handler = Handler(Looper.getMainLooper())

    init {
        startExitDateCheck()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_info_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fall_down)
        )
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val buildingNameTextView: TextView =
            itemView.findViewById(R.id.userDetailsBuildingName)
        private val roomNumberTextView: TextView =
            itemView.findViewById(R.id.userDetailsroomNumberTextView)
        private val floorNumberTextView: TextView =
            itemView.findViewById(R.id.userDetailsfloorNumberTextView)
        private val bedTypeTextView: TextView =
            itemView.findViewById(R.id.userDetailsbedTypeTextView)
        private val roomBookNowButton: ImageView = itemView.findViewById(R.id.roomBookNow)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    showItemDetailsDialog(item)
                }
            }

            roomBookNowButton.setOnClickListener {
                showCancelBookingConfirmation()
            }
        }

        private fun showCancelBookingConfirmation() {
            AlertDialog.Builder(itemView.context)
                .setMessage("Are you sure you want to cancel the booking?")
                .setPositiveButton("Yes") { _, _ ->
                    val item = getItem(adapterPosition)
                    item?.let {
                        viewModel.cancelBooking(it.id)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        private fun showItemDetailsDialog(item: Item) {
            val dialogView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.dialog_item_details, null)

            dialogView.findViewById<TextView>(R.id.buildingNameTextView)
                .text = item.roomBuildingName
            dialogView.findViewById<TextView>(R.id.roomNumberTextView)
                .text = item.roomNumber
            dialogView.findViewById<TextView>(R.id.floorNumberTextView)
                .text = item.floorNumber
            dialogView.findViewById<TextView>(R.id.bedTypeTextView)
                .text = item.bedType
            dialogView.findViewById<TextView>(R.id.nameTextView)
                .text = item.name
            dialogView.findViewById<TextView>(R.id.detailsTextView)
                .text = item.details
            dialogView.findViewById<TextView>(R.id.entryDateTextView)
                .text = item.entryDate
            dialogView.findViewById<TextView>(R.id.exitDateTextView)
                .text = item.exitDate

            AlertDialog.Builder(itemView.context)
                .setView(dialogView)
                .setPositiveButton("OK", null)
                .show()
        }

        fun bind(item: Item) {
            buildingNameTextView.text = item.roomBuildingName
            roomNumberTextView.text = item.roomNumber
            floorNumberTextView.text = item.floorNumber
            bedTypeTextView.text = item.bedType
        }
    }


    private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    private fun startExitDateCheck() {
        handler.postDelayed(exitDateCheckRunnable, EXIT_DATE_CHECK_INTERVAL)
    }

    private fun checkExitDatesAndCancelBooking(position: Int) {
        val currentDate = Calendar.getInstance().timeInMillis
        val item = getItem(position)

        Log.d("AutoCancelDebug", "Checking exit date for item: $item")

        if (!item.exitDate.isNullOrEmpty()) {
            val exitDateInMillis = item.exitDate.toLongOrNull()
            Log.d("AutoCancelDebug", "Exit date in milliseconds: $exitDateInMillis")
            if (exitDateInMillis != null && exitDateInMillis < currentDate) {
                item.let {
                    viewModel.cancelBooking(it.id)
                    Log.d("AutoCancelDebug", "Booking cancelled for item: $item")
                }
            }
        }
    }


    private val exitDateCheckRunnable = object : Runnable {
        override fun run() {
            currentList.forEachIndexed { index, _ ->
                checkExitDatesAndCancelBooking(index)
            }
            handler.postDelayed(this, EXIT_DATE_CHECK_INTERVAL)
        }
    }

    companion object {
        private const val EXIT_DATE_CHECK_INTERVAL = 24 * 60 * 60 * 1000L
    }
}

