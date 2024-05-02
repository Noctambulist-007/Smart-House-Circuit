package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentHomeBinding
import com.algostack.smartcircuithouse.features.home_screen.HomeScreenDirections
import com.algostack.smartcircuithouse.features.home_screen.adapter.BuildingAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingData
import com.algostack.smartcircuithouse.services.db.BuildingDB

class HomeFragment : Fragment(), BuildingAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BuildingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BuildingAdapter(this)
        binding.allRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.allRecyclerView.adapter = adapter

        val buildingDao = BuildingDB.getDatabase(requireContext()).buildingDao()

        binding.homeProgressBar.visibility = View.VISIBLE

        buildingDao.getAllBuildings().observe(viewLifecycleOwner, Observer { buildings ->

            if (buildings.isEmpty()) {
                binding.textViewNoItems.visibility = View.VISIBLE
                binding.homeProgressBar.visibility = View.GONE
                binding.textViewTotalBuildingItems.text = "Total Items: ${buildings.size}"
            } else {
                binding.homeProgressBar.visibility = View.GONE
                adapter.submitList(buildings.map {
                    BuildingData(
                        id = it.id,
                        imageResource = R.drawable.building,
                        title = it.name,
                        primaryKey = it.primaryKey
                    )
                })
                binding.textViewTotalBuildingItems.text = "Total Items: ${buildings.size}"
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(title: String, buildingId: String, primaryKey: String) {
        val action = HomeScreenDirections.actionHomeScreenToRoomScreen(
            title,
            buildingId,
            primaryKey
        )
        findNavController().navigate(action)
    }
}
