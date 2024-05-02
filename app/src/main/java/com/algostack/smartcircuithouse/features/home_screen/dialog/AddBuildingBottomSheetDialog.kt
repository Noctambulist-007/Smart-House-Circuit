import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingViewModel
import com.algostack.smartcircuithouse.services.db.BuildingDB
import com.algostack.smartcircuithouse.services.db.BuildingDao
import com.algostack.smartcircuithouse.services.model.BuildingData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBuildingBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: BuildingViewModel by viewModels()
    private lateinit var buildingDao: BuildingDao
    private lateinit var etBuildingName: TextInputEditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        buildingDao = BuildingDB.getDatabase(context).buildingDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_building, container, false)
        etBuildingName = view.findViewById(R.id.etBuildingName)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val buildingName = etBuildingName.text.toString().trim()

            if (buildingName.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val existingBuilding = buildingDao.getBuildingByName(buildingName)
                    if (existingBuilding != null) {
                        showSnackbar(view, "Building with the same name already exists")
                    } else {
                        val building = BuildingData(name = buildingName)
                        viewModel.saveBuilding(requireContext(), building.name)
                        dismiss()
                    }
                }
            } else {
                showSnackbar(view, "Please enter a building name")
            }
        }

        return view
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
            .setTextColor(ContextCompat.getColor(view.context, R.color.white))
            .show()
    }
}
