package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import com.algostack.smartcircuithouse.R

class FilteredItemsAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<FilteredItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_info_filter_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val buildingNameTextView: TextView =
            itemView.findViewById(R.id.buildingNameTextView)
        private val roomNumberTextView: TextView = itemView.findViewById(R.id.roomNumberTextView)
        private val floorNumberTextView: TextView = itemView.findViewById(R.id.floorNumberTextView)
        private val bedTypeTextView: TextView = itemView.findViewById(R.id.bedTypeTextView)

        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        private val entryDateTextView: TextView = itemView.findViewById(R.id.entryDateTextView)
        private val exitDateTextView: TextView = itemView.findViewById(R.id.exitDateTextView)

        fun bind(item: Item) {
            buildingNameTextView.text = item.roomBuildingName
            roomNumberTextView.text = item.roomNumber
            floorNumberTextView.text = item.floorNumber
            bedTypeTextView.text = item.bedType

            nameTextView.text = item.name
            detailsTextView.text = item.details
            entryDateTextView.text = item.entryDate
            exitDateTextView.text = item.exitDate

        }
    }
}
