package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.pregnant_women_registration.form

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.PregnantWomanRegistrationDataset
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.MaternalHealthRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PregnancyRegistrationFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val maternalHealthRepo: MaternalHealthRepo,
    private val benRepo: BenRepo
//    private val householdRepo: HouseholdRepo,
//    userRepo: UserRepo
) : ViewModel() {

    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }

    private val benId =
        PregnancyRegistrationFormFragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender

    private val _recordExists = MutableLiveData<Boolean>()
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    //    private lateinit var user: UserDomain
    private val dataset =
        PregnantWomanRegistrationDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow

    private lateinit var pregnancyRegistrationForm: PregnantWomanRegistrationCache

    init {
        viewModelScope.launch {
            val ben = maternalHealthRepo.getBenFromId(benId)?.also { ben ->
                _benName.value =
                    "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
                _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
                pregnancyRegistrationForm = PregnantWomanRegistrationCache(
                    benId = ben.beneficiaryId,
                )
            }

            maternalHealthRepo.getSavedRegistrationRecord(benId)?.let {
                pregnancyRegistrationForm = it
                _recordExists.value = true
            } ?: run {
                _recordExists.value = false
            }

            dataset.setUpPage(
                ben,
                if (recordExists.value == true) pregnancyRegistrationForm else null
            )


        }
    }

    fun updateListOnValueChanged(formId: Int, index: Int) {
        viewModelScope.launch {
            dataset.updateList(formId, index)
        }

    }

    fun getIndexOfEdd(): Int = dataset.getIndexOfEdd()
    fun getIndexOfWeeksOfPregnancy(): Int = dataset.getIndexOfWeeksPregnancy()
    fun getIndexOfPastIllness(): Int = dataset.getIndexOfPastIllness()

    fun saveForm() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _state.postValue(State.SAVING)

                    dataset.mapValues(pregnancyRegistrationForm, 1)
                    maternalHealthRepo.persistRegisterRecord(pregnancyRegistrationForm)
                    maternalHealthRepo.getBenFromId(benId)?.let {
                        val hasBenUpdated = dataset.mapValueToBenRegId(it)
                        if (hasBenUpdated)
                            benRepo.persistRecord(it)
                    }
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: Exception) {
                    Timber.d("saving PWR data failed!!")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }
    }

    fun setRecordExist(b: Boolean) {
        _recordExists.value = b

    }


}