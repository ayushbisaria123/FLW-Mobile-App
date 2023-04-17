package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.ImmunizationGridAdapter
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class ImmunizationListFragment : Fragment() {

    private val viewModel: ImmunizationListViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })
    private var _binding : RvIconGridBinding? = null
    private val binding : RvIconGridBinding
    get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RvIconGridBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpImmunizationIconRvAdapter()
    }

    private fun setUpImmunizationIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, requireContext().resources.getInteger(R.integer.icon_grid_span))
        binding.rvIconGrid.layoutManager = rvLayoutManager
        val iconAdapter = ImmunizationGridAdapter(
            ImmunizationGridAdapter.ImmunizationIconClickListener {a,b,c,d,e ->
//                findNavController().navigate(
//                    I.actionImmunizationListFragmentToImmunizationObjectFragment(a, b, c, d, e))
            })
        binding.rvIconGrid.adapter = iconAdapter

//        iconAdapter.submitList(viewModel.vaccineList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}