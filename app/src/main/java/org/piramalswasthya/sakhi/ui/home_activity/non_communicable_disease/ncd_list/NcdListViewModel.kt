package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class NcdListViewModel @Inject constructor(
    recordsRepo: RecordsRepo,
    userRepo: UserRepo,
) : ViewModel(

) {

    private lateinit var asha : UserDomain
    private val allBenList = recordsRepo.getNcdList()
    private val filter = MutableStateFlow("")
    private val selectedBenId = MutableStateFlow(0L)
    val benList = allBenList.combine(filter) { list, filter ->
        filterBenList(list.map { it.ben.asBasicDomainModel() }, filter)
    }

    val ncdDetails = allBenList.combineTransform(selectedBenId) { list, benId ->
        if (benId > 0) {
            val list = list.firstOrNull { it.ben.benId == benId }?.savedCbacRecords
            if (!list.isNullOrEmpty()) emit(list)
        }
    }

    init {
        viewModelScope.launch {
            asha = userRepo.getLoggedInUser()!!
        }
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

    fun setSelectedBenId(benId: Long) {
        viewModelScope.launch {
            selectedBenId.emit(benId)
        }
    }

    fun getSelectedBenId(): Long = selectedBenId.value
    fun getAshaId(): Int  = asha.userId


}