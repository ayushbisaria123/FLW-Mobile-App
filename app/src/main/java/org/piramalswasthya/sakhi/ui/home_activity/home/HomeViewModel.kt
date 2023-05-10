package org.piramalswasthya.sakhi.ui.home_activity.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.ImageUtils
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    database: InAppDb,
    private val pref: PreferenceDao,
    private val userRepo: UserRepo,
) : ViewModel() {


    val currentUser = database.userDao.getLoggedInUserLiveData()

    val numBenIdsAvail = database.benIdGenDao.liveCount()

    val scope: CoroutineScope
        get() = viewModelScope
    private var _unprocessedRecords : Int = 0
    val unprocessedRecords: Int
        get() = _unprocessedRecords



    private var locationRecord: LocationRecord? = null

    fun setLocationDetails(
        state: String,
        district: String,
        block: String,
        village: String
    ) {
        val stateId = user.stateIds[user.stateEnglish.indexOf(state)]
        val districtId = user.districtIds[user.districtEnglish.indexOf(district)]
        val blockId = user.blockIds[user.blockEnglish.indexOf(block)]
        val villageId = user.villageIds[user.villageEnglish.indexOf(village)]
        this.locationRecord = LocationRecord(
            stateId,
            state,
            districtId,
            district,
            blockId,
            block,
            villageId,
            village,
            user.countryId
        )
        pref.saveLocationRecord(locationRecord!!)
    }

    fun getLocationRecord() = locationRecord!!


    fun isLocationSet(): Boolean {
        return locationRecord != null
    }

    private lateinit var _user: UserDomain
    val user: UserDomain
        get() = _user

    private val _navigateToLoginPage = MutableLiveData(false)
    val navigateToLoginPage: MutableLiveData<Boolean>
        get() = _navigateToLoginPage

    init {
        viewModelScope.launch {
            _user = getUserFromRepo()
            launch {
                userRepo.unProcessedRecordCount.collect { value ->
                    _unprocessedRecords = value
                }
            }
        }
    }

    private suspend fun getUserFromRepo(): UserDomain {
        return withContext(Dispatchers.IO) {
            userRepo.getLoggedInUser()
                ?: throw IllegalStateException("No Logged in user found in DB!!")
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
            pref.setLastSyncedTimeStamp(Konstants.defaultTimeStamp)
            pref.deleteForLogout()


            _navigateToLoginPage.value = true
        }
    }

    fun navigateToLoginPageComplete() {
        _navigateToLoginPage.value = false
    }

    fun checkIfFullLoadCompletedBefore(): Long {
        return pref.getLastSyncedTimeStamp()

    }

    fun saveProfilePicUri(imageUri: Uri) {
        pref.saveProfilePicUri(imageUri)
    }

    fun getProfilePicUri(): Uri? {
        return pref.getProfilePicUri()
    }


}