package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.mother_immunization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class MotherImmunizationViewModel @Inject constructor(
    recordsRepo: RecordsRepo
) : ViewModel() {

    private val allBenList = recordsRepo.motherImmunizationList
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter){
            list, filter -> filterBenList(list, filter)
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }
}