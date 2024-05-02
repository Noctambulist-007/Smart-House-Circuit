package com.algostack.smartcircuithouse.features.room_screen.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.android.material.snackbar.Snackbar

class RoomAdapter(
    private val onBookNowClickListener: OnBookNowClickListener,
    private var onDeleteClickListener: OnDeleteClickListener?
) :
    ListAdapter<RoomData, RoomAdapter.RoomViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RoomData>() {
            override fun areItemsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view, onBookNowClickListener, onDeleteClickListener)
    }


    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.setAnimation(holder.itemView, position)

        holder.itemView.setOnLongClickListener {
            holder.showDeleteConfirmationDialog(currentItem) { message ->
                showSnackbar(holder.itemView, message)
            }
            true
        }
    }

    private var lastPosition = -1

    interface OnDeleteClickListener {
        fun onDeleteClick(roomData: RoomData)
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.onDeleteClickListener = listener
    }

    inner class RoomViewHolder(
        itemView: View,
        private val onBookNowClickListener: OnBookNowClickListener,
        private val onDeleteClickListener: OnDeleteClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        private val roomBuildingNameTextView: TextView =
            itemView.findViewById(R.id.textViewRoomBuildingName)
        private val roomNoTextView: TextView = itemView.findViewById(R.id.textViewRoomNo)
        private val bedTypeTextView: TextView = itemView.findViewById(R.id.textViewBedType)
        private val floorNoTextView: TextView = itemView.findViewById(R.id.textViewFloorNo)
        private val bookNowButton: Button = itemView.findViewById(R.id.roomBookNow)

        fun bind(roomData: RoomData) {
            roomBuildingNameTextView.text = roomData.roomBuildingName
            floorNoTextView.text = roomData.floorNo
            roomNoTextView.text = roomData.roomNo
            bedTypeTextView.text = roomData.bedType

            if (roomData.isBooked) {
                bookNowButton.text = itemView.context.getString(R.string.booked)
                bookNowButton.setBackgroundColor(itemView.context.getColor(R.color.red))
            } else {
                bookNowButton.text = itemView.context.getString(R.string.book_now)
                bookNowButton.setBackgroundColor(itemView.context.getColor(R.color.primary))
            }

            bookNowButton.setOnClickListener {
                if (roomData.isBooked) {
                    showCancelBookingConfirmation(roomData)
                } else {
                    onBookNowClickListener.onBookNowClick(roomData)
                }
            }
        }

        fun setAnimation(viewToAnimate: View, position: Int) {
            if (position > lastPosition) {
                val animation: Animation =
                    AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.fall_down)
                viewToAnimate.startAnimation(animation)
                lastPosition = position
            }
        }

        fun showDeleteConfirmationDialog(
            roomData: RoomData,
            showSnackbarCallback: (String) -> Unit
        ) {
            AlertDialog.Builder(itemView.context)
                .setMessage("Are you sure you want to delete this room?")
                .setPositiveButton("Yes") { _, _ ->
                    onDeleteClickListener?.onDeleteClick(roomData)
                    showSnackbarCallback("Room deleted successfully")
                }
                .setNegativeButton("No", null)
                .show()
        }


        private fun showCancelBookingConfirmation(roomData: RoomData) {
            AlertDialog.Builder(itemView.context)
                .setMessage("Are you sure you want to cancel the booking?")
                .setPositiveButton("Yes") { _, _ ->
                    onBookNowClickListener.onCancelBookingClick(roomData)
                }
                .setNegativeButton("No", null)
                .show()
        }

    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
            .setTextColor(ContextCompat.getColor(view.context, R.color.white))
            .show()
    }

    interface OnBookNowClickListener {
        fun onBookNowClick(roomData: RoomData)
        fun onCancelBookingClick(roomData: RoomData)
    }

    fun updateRoomStatus(position: Int) {
        val roomData = currentList[position]
        roomData.isBooked = true
        notifyItemChanged(position)
    }
}
