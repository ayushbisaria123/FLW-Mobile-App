package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.ChildDeathReviewFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.CdrRepo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CdrObjectViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
    private val database: InAppDb,
    private val cdrRepo: CdrRepo,
    private val benRepo: BenRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    private val benId = CdrObjectFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = CdrObjectFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache
    private lateinit var user: UserCache

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val dataset = ChildDeathReviewFormDataset(context)

    private fun toggleFieldOnTrigger(
        causeField: FormInput,
        effectField: FormInput,
        value: String?,
        adapter: FormInputAdapter
    ) {
        value?.let {
            if (it == "Hospital") {
                val list = adapter.currentList.toMutableList()
                if(!list.contains(effectField)) {
                    list.add(
                        adapter.currentList.indexOf(causeField) + 1,
                        effectField
                    )
                    adapter.submitList(list)
                }
            } else {
                if (adapter.currentList.contains(effectField)) {
                    val list = adapter.currentList.toMutableList()
                    list.remove(effectField)
                    adapter.submitList(list)
                }
            }
        }
    }

    fun submitForm() {
        val cdrCache = CDRCache(benId = benId, hhId = hhId, processed = "N", createdBy = user.userName, age = ben.age)
        dataset.mapValues(cdrCache)
        viewModelScope.launch {
            val saved = cdrRepo.saveCdrData(cdrCache)
            if (saved)
                _state.value = State.SUCCESS
            else
                _state.value = State.FAIL
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = benRepo.getBeneficiary(benId, hhId)!!
                household = benRepo.getBenHousehold(hhId)!!
                user = database.userDao.getLoggedInUser()!!
            }
            _benName.value = ben.firstName + ben.lastName
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)

        }
    }

    private fun getAddress(household: HouseholdCache): String {
        val houseNo = household.houseNo
        val wardNo = household.wardNo
        val name = household.wardName
        val mohalla = household.mohallaName
        val district = household.district
        val city = household.village
        val state = household.state

        var address = "$houseNo, $wardNo, $name, $mohalla, $city, $district, $state"
        address = address.replace(", ,", ",")
        address = address.replace(",,", ",")
        address = address.replace(" ,", "")
        address = address.replace("null, ", "")
        address = address.replace(", null", "")

        return address
    }

    suspend fun getFirstPage(adapter: FormInputAdapter): List<FormInput> {
        viewModelScope.launch {
            dataset.placeOfDeath.value.collect {
                toggleFieldOnTrigger(
                    dataset.placeOfDeath,
                    dataset.hospitalName,
                    it,
                    adapter
                )
            }
        }
        return dataset.firstPage
    }

    fun setAddress(it: String?, adapter: FormInputAdapter) {
        dataset.address.value.value = it
        dataset.childName.value.value = ben.firstName
        dataset.gender.value.value = when (ben.gender){
            Gender.MALE -> "Male"
            Gender.FEMALE -> "Female"
            Gender.TRANSGENDER -> "Transgender"
            else -> "Other"
        }
        dataset.age.value.value = "${ben.age} ${ben.ageUnit?.name}"
        dataset.dateOfBirth.value.value = getDateFromLong(ben.dob)
        dataset.firstInformant.value.value = user.userName
        dataset.dateOfNotification.value.value = getDateFromLong(System.currentTimeMillis())
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.address))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.childName))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.gender))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.age))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.dateOfBirth))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.firstInformant))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.dateOfNotification))
    }

    private fun getDateFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }

    }
}