package org.piramalswasthya.sakhi.ui.home_activity.all_household.household_members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenHealthIdDetails
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject


@HiltViewModel
class HouseholdMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val benRepo: BenRepo,

    ) : ViewModel() {

    val hhId = HouseholdMembersFragmentArgs.fromSavedStateHandle(savedStateHandle).hhId

    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    private val _abha = MutableLiveData<String?>()
    val abha: LiveData<String?>
        get() = _abha

    private val _benId = MutableLiveData<Long?>()
    val benId: LiveData<Long?>
        get() = _benId

    private val _benRegId = MutableLiveData<Long?>()
    val benRegId: LiveData<Long?>
        get() = _benRegId

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _benList.postValue(
                    benRepo.getBenBasicListFromHousehold(hhId).map { it.asBasicDomainModel() })
            }
        }
    }

    fun fetchAbha(benId: Long) {
        _abha.value = null
        _benRegId.value = null
        _benId.value = benId
        viewModelScope.launch {
            benRepo.getBenFromId(benId)?.let {
                val result = benRepo.getBeneficiaryWithId(it.benRegId)
                if (result != null) {
                    _abha.value = result.healthIdNumber
                    it.healthIdDetails = BenHealthIdDetails(result.healthId, result.healthIdNumber)
                    benRepo.updateRecord(it)
                } else {
                    _benRegId.value = it.benRegId
                }
            }
        }
    }

    fun resetBenRegId() {
        _benRegId.value = null
    }


}