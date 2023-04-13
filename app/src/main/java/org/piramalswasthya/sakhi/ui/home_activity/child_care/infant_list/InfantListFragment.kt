package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.adapters.BenListAdapterForForm
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.NewBenRegTypeFragment
import org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr.CdrListFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber

@AndroidEntryPoint
class InfantListFragment : Fragment() {

    private var _binding: FragmentDisplaySearchRvButtonBinding? = null
    private val binding : FragmentDisplaySearchRvButtonBinding
        get() = _binding!!

    private val viewModel: InfantListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding  = FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextPage.visibility = View.GONE
        val benAdapter = BenListAdapterForForm(
            BenListAdapterForForm.ClickListener(
                {
                    Toast.makeText(context, "Ben : $it clicked", Toast.LENGTH_SHORT).show()
                },
                { hhId, benId ->
                    Timber.d("benId : $benId hhId : $hhId")
                    findNavController().navigate(
                        InfantListFragmentDirections.actionInfantListFragmentToHbncDayListFragment(
                            hhId = hhId,
                            benId = benId
                        )
                    )
                }), "HBNC Form")
        binding.rvAny.adapter = benAdapter

        lifecycleScope.launch {
            viewModel.benList.collect{
                if (it.isEmpty())
                    binding.flEmpty.visibility = View.VISIBLE
                else
                    binding.flEmpty.visibility = View.GONE
                benAdapter.submitList(it)
            }
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.filterText(p0?.toString() ?: "")
            }

        }
        binding.searchView.setOnFocusChangeListener { searchView, b ->
            if (b)
                (searchView as EditText).addTextChangedListener(searchTextWatcher)
            else
                (searchView as EditText).removeTextChangedListener(searchTextWatcher)

        }
    }
    override fun onStart() {
        super.onStart()
        activity?.let{
            (it as HomeActivity).setLogo(R.drawable.ic_infant)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}