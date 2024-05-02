package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingData
import com.algostack.smartcircuithouse.services.db.BuildingDB
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuildingAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<BuildingData, BuildingAdapter.CardViewHolder>(DiffCallback) {
    interface OnItemClickListener {
        fun onItemClick(title: String, buildingId: String, primaryKey: String)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BuildingData>() {
            override fun areItemsTheSame(oldItem: BuildingData, newItem: BuildingData): Boolean {
                return oldItem.primaryKey == newItem.primaryKey
            }

            override fun areContentsTheSame(oldItem: BuildingData, newItem: BuildingData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_building, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fall_down)
        )
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

        fun bind(item: BuildingData) {
            imageView.setImageResource(item.imageResource)
            textViewTitle.text = item.title

            itemView.setOnClickListener {
                itemClickListener.onItemClick(item.title, item.id, item.primaryKey)
            }

            itemView.setOnLongClickListener {
                showDeleteConfirmationDialog(itemView.context, item)
                true
            }
        }

        private fun showDeleteConfirmationDialog(context: Context, item: BuildingData) {
            AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteItem(item)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun deleteItem(item: BuildingData) {
            val context = itemView.context
            val buildingDao = BuildingDB.getDatabase(context).buildingDao()
            val roomDao = RoomDB.getDatabase(context).roomDao()

            CoroutineScope(Dispatchers.IO).launch {

                roomDao.deleteRoomsForBuilding(item.primaryKey)

                buildingDao.delete(item.primaryKey)

            }
            showSnackbar(itemView, "Building and associated rooms deleted successfully")
        }


        private fun showSnackbar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
                .setTextColor(ContextCompat.getColor(view.context, R.color.white))
                .show()
        }
    }
}
