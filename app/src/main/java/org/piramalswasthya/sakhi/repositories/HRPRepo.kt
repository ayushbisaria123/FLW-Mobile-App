package org.piramalswasthya.sakhi.repositories

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.EcrPost
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache
import org.piramalswasthya.sakhi.model.HRPMicroBirthPlanCache
import org.piramalswasthya.sakhi.model.HRPNonPregnantAssessCache
import org.piramalswasthya.sakhi.model.HRPNonPregnantTrackCache
import org.piramalswasthya.sakhi.model.HRPPregnantAssessCache
import org.piramalswasthya.sakhi.model.HRPPregnantTrackCache
import org.piramalswasthya.sakhi.model.HighRiskAssessDTO
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache
import org.piramalswasthya.sakhi.model.PwrPost
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.GetDataPaginatedRequest
import org.piramalswasthya.sakhi.network.HRPNonPregnantAssessDTO
import org.piramalswasthya.sakhi.network.HRPNonPregnantTrackDTO
import org.piramalswasthya.sakhi.network.HRPPregnantAssessDTO
import org.piramalswasthya.sakhi.network.HRPPregnantTrackDTO
import org.piramalswasthya.sakhi.network.UserDataDTO
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class HRPRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val maternalHealthRepo: MaternalHealthRepo,
    private val ecrRepo: EcrRepo,
    private val preferenceDao: PreferenceDao,
    private val tmcNetworkApiService: AmritApiService

) {
    suspend fun getPregnantAssess(benId: Long): HRPPregnantAssessCache? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getPregnantAssess(benId)
        }
    }

    suspend fun saveRecord(hrpPregnantAssessCache: HRPPregnantAssessCache) {
        withContext(Dispatchers.IO) {
            database.hrpDao.saveRecord(hrpPregnantAssessCache)
        }
    }

    suspend fun getNonPregnantAssess(benId: Long): HRPNonPregnantAssessCache? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getNonPregnantAssess(benId)
        }
    }

    suspend fun saveRecord(hrpNonPregnantAssessCache: HRPNonPregnantAssessCache) {
        withContext(Dispatchers.IO) {
            database.hrpDao.saveRecord(hrpNonPregnantAssessCache)
        }
    }

    suspend fun getNonPregnantTrackList(benId: Long): List<HRPNonPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getNonPregnantTrackList(benId)
        }
    }

    suspend fun saveRecord(hrpNonPregnantTrackCache: HRPNonPregnantTrackCache) {
        withContext(Dispatchers.IO) {
            database.hrpDao.saveRecord(hrpNonPregnantTrackCache)
        }
    }

    suspend fun getPregnantTrackList(benId: Long): List<HRPPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getPregnantTrackList(benId)
        }
    }

    suspend fun getHRPTrack(trackId: Long): HRPPregnantTrackCache? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getHRPTrack(trackId)
        }
    }

    suspend fun getHRPNonTrack(trackId: Long): HRPNonPregnantTrackCache? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getHRPNonTrack(trackId)
        }
    }

    suspend fun saveRecord(hrpPregnantTrackCache: HRPPregnantTrackCache) {
        withContext(Dispatchers.IO) {
            database.hrpDao.saveRecord(hrpPregnantTrackCache)
        }
    }

    suspend fun getMicroBirthPlan(benId: Long): HRPMicroBirthPlanCache? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getMicroBirthPlan(benId)
        }
    }

    suspend fun saveRecord(hrpMicroBirthPlanCache: HRPMicroBirthPlanCache) {
        withContext(Dispatchers.IO) {
            database.hrpDao.saveRecord(hrpMicroBirthPlanCache)
        }
    }

    suspend fun getAllPregTrack(): List<HRPPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getAllPregTrack()
        }
    }

    suspend fun getHrPregTrackList(benId: Long): List<HRPPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getAllPregTrackforBen(benId)
        }
    }

    suspend fun getAllNonPregTrack(): List<HRPNonPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getAllNonPregTrack()
        }
    }

    suspend fun getHrNonPregTrackList(benId: Long): List<HRPNonPregnantTrackCache>? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getAllNonPregTrackforBen(benId)
        }
    }

    suspend fun getMaxLmp(benId: Long): Long? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getMaxLmp(benId)
        }
    }

    suspend fun getMaxDoVNonHrp(benId: Long): Long? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getMaxDoV(benId)
        }
    }

    suspend fun getMaxDoVHrp(benId: Long): Long? {
        return withContext(Dispatchers.IO) {
            database.hrpDao.getMaxDoVhrp(benId)
        }
    }

    suspend fun getHighRiskAssessDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response = tmcNetworkApiService.getHighRiskAssessData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(Konstants.defaultTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit assess data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveHighRiskAssess(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("Assess entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get data error : $e")
                return@withContext getHighRiskAssessDetailsFromServer()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get data error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun saveHighRiskAssess(dataObj: String) {
        val requestDTO = Gson().fromJson(dataObj, JsonObject::class.java)
        val entries = requestDTO.getAsJsonArray("entries")
        for (dto in entries) {
            try {
                val entry = Gson().fromJson(dto.toString(), HighRiskAssessDTO::class.java)
                entry.createdDate?.let {
                    val hrpPregnantAssessCache = database
                        .hrpDao.getPregnantAssess(entry.benId)
                    if (hrpPregnantAssessCache == null) {
                        database.hrpDao.saveRecord(entry.toPregnantAssess())
                    } else {
                        if (hrpPregnantAssessCache.noOfDeliveries == null) hrpPregnantAssessCache.noOfDeliveries =
                            entry.noOfDeliveries
                        if (hrpPregnantAssessCache.timeLessThan18m == null) hrpPregnantAssessCache.timeLessThan18m =
                            entry.timeLessThan18m
                        if (hrpPregnantAssessCache.heightShort == null) hrpPregnantAssessCache.heightShort =
                            entry.heightShort
                        if (hrpPregnantAssessCache.age == null) hrpPregnantAssessCache.age =
                            entry.age
                        isHighRisk(hrpPregnantAssessCache)
                        database.hrpDao.saveRecord(hrpPregnantAssessCache)
                    }

                    val hrpNonPregnantAssessCache = database
                        .hrpDao.getNonPregnantAssess(entry.benId)
                    if (hrpNonPregnantAssessCache == null) {
                        database.hrpDao.saveRecord(entry.toNonPregnantAssess())
                    } else {
                        if (hrpNonPregnantAssessCache.noOfDeliveries == null) hrpNonPregnantAssessCache.noOfDeliveries =
                            entry.noOfDeliveries
                        if (hrpNonPregnantAssessCache.timeLessThan18m == null) hrpNonPregnantAssessCache.timeLessThan18m =
                            entry.timeLessThan18m
                        if (hrpNonPregnantAssessCache.heightShort == null) hrpNonPregnantAssessCache.heightShort =
                            entry.heightShort
                        if (hrpNonPregnantAssessCache.age == null) hrpNonPregnantAssessCache.age =
                            entry.age
                        isHighRisk(hrpNonPregnantAssessCache)
                        database.hrpDao.saveRecord(hrpNonPregnantAssessCache)
                    }

                }
            } catch (e: java.lang.Exception) {
                Timber.d("cannot save entry $dto due to : $e")
            }
        }
    }

    private fun isHighRisk(assess: HRPNonPregnantAssessCache) {
        assess.isHighRisk =
            assess.noOfDeliveries == "Yes" ||
                    assess.timeLessThan18m == "Yes" ||
                    assess.heightShort == "Yes" ||
                    assess.age == "Yes" ||
                    assess.misCarriage == "Yes" ||
                    assess.homeDelivery == "Yes" ||
                    assess.medicalIssues == "Yes" ||
                    assess.pastCSection == "Yes"
    }

    private fun isHighRisk(assess: HRPPregnantAssessCache) {
        assess.isHighRisk =
            assess.noOfDeliveries == "Yes" ||
                    assess.timeLessThan18m == "Yes" ||
                    assess.heightShort == "Yes" ||
                    assess.age == "Yes" ||
                    assess.rhNegative == "Yes" ||
                    assess.homeDelivery == "Yes" ||
                    assess.badObstetric == "Yes" ||
                    assess.multiplePregnancy == "Yes"
    }

    suspend fun getHRPAssessDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response = tmcNetworkApiService.getHRPAssessData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(Konstants.defaultTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit hrp assess data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveHRPAssess(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("HRP Assess entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get data error : $e")
                return@withContext getHRPAssessDetailsFromServer()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get data error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun saveHRPAssess(dataObj: String) {
        val requestDTO = Gson().fromJson(dataObj, JsonObject::class.java)
        val entries = requestDTO.getAsJsonArray("entries")
        for (dto in entries) {
            try {
                val entry = Gson().fromJson(dto.toString(), HRPPregnantAssessDTO::class.java)
                entry.visitDate?.let {
                    val hrpPregnantAssessCache = database
                        .hrpDao.getPregnantAssess(entry.benId)
                    if (hrpPregnantAssessCache == null) {
                        database.hrpDao.saveRecord(entry.toCache())
                    }
                }
            } catch (e: java.lang.Exception) {
                Timber.d("cannot save entry $dto due to : $e")
            }
        }
    }

    suspend fun getHRPTrackDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response = tmcNetworkApiService.getHRPTrackData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(Konstants.defaultTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit hrp track data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveHRPTrack(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("HRP Track entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get data error : $e")
                return@withContext getHRPTrackDetailsFromServer()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get data error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun saveHRPTrack(dataObj: String) {
        val requestDTO = Gson().fromJson(dataObj, JsonObject::class.java)
        val entries = requestDTO.getAsJsonArray("entries")
        for (dto in entries) {
            try {
                val entry = Gson().fromJson(dto.toString(), HRPPregnantTrackDTO::class.java)
                entry.visitDate?.let {
                    val track = entry.visit?.let { it1 ->
                        database
                            .hrpDao.getHRPTrack(
                                entry.benId,
                                it1,
                                getLongFromDate(it),
                                getLongFromDate(it) - 19_800_000
                            )
                    }
                    if (track == null) {
                        database.hrpDao.saveRecord(entry.toCache())
                    }
                }
            } catch (e: java.lang.Exception) {
                Timber.d("couldn't save $dto due to $e")
            }

        }
    }

    suspend fun getHRNonPAssessDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response = tmcNetworkApiService.getHRNonPAssessData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(Konstants.defaultTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from Amrit hrp non pregnant Assess data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveHRNonPAssess(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("HRP non pregnant Assess entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get data error : $e")
                return@withContext getHRNonPAssessDetailsFromServer()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get data error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun saveHRNonPAssess(dataObj: String) {
        val requestDTO = Gson().fromJson(dataObj, JsonObject::class.java)
        val entries = requestDTO.getAsJsonArray("entries")
        for (dto in entries) {
            try {
                val entry = Gson().fromJson(dto.toString(), HRPNonPregnantAssessDTO::class.java)
                entry.visitDate?.let {
                    val hrpPregnantAssessCache = database
                        .hrpDao.getNonPregnantAssess(entry.benId)
                    if (hrpPregnantAssessCache == null) {
                        database.hrpDao.saveRecord(entry.toCache())
                    }
                }
            } catch (e: java.lang.Exception) {
                Timber.d("couldn't save $dto due to $e")
            }
        }
    }

    suspend fun getHRNonPTrackDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response = tmcNetworkApiService.getHRNonPTrackData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(Konstants.defaultTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit non hrp track data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveHRNonPTrack(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("non HRP Track entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get data error : $e")
                return@withContext getHRNonPTrackDetailsFromServer()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get data error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun saveHRNonPTrack(dataObj: String) {
        val requestDTO = Gson().fromJson(dataObj, JsonObject::class.java)
        val entries = requestDTO.getAsJsonArray("entries")
        for (dto in entries) {
            try {
                val entry = Gson().fromJson(dto.toString(), HRPNonPregnantTrackDTO::class.java)
                entry.visitDate?.let {
                    val track = database
                        .hrpDao.getHRPNonTrack(
                            entry.benId,
                            getLongFromDate(it),
                            getLongFromDate(it) - 19_800_000
                        )
                    if (track == null) {
                        database.hrpDao.saveRecord(entry.toCache())
                    }
                }
            } catch (e: java.lang.Exception) {
                Timber.d("couldn't save $dto due to $e")
            }

        }
    }

    suspend fun pushUnSyncedRecords(): Boolean {
        val hrpaResult = pushUnSyncedRecordsHRPAssess()
        val hrptResult = pushUnSyncedRecordsHRPTrack()
        val hrnpaResult = pushUnSyncedRecordsHRNonPAssess()
        val hrnptResult = pushUnSyncedRecordsHRNonPTrack()
        return (hrpaResult == 1) && (hrptResult == 1) && (hrnpaResult == 1) && (hrnptResult == 1)
    }

    private suspend fun pushUnSyncedRecordsHRPAssess(): Int {

        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val entities = database.hrpDao.getHRPAssess(SyncState.UNSYNCED)

            val assessDtos = mutableListOf<Any>()

            val pwrDtos = mutableSetOf<PwrPost>()

            entities?.let {
                it.forEach { cache ->
                    assessDtos.add(cache.toHighRiskAssessDTO())
                    pwrDtos.add(mapPWR(cache))
                    cache.syncState = SyncState.SYNCED
                }
            }

            if (maternalHealthRepo.postPwrToAmritServer(pwrDtos)) {
                pwrDtos.forEach { pwr ->
                    maternalHealthRepo.getSavedRegistrationRecord(pwr.benId)?.let { pwrCache ->
                        pwrCache.processed = "P"
                        pwrCache.syncState = SyncState.SYNCED
                        maternalHealthRepo.persistRegisterRecord(pwrCache)
                    }
                }
            }

            if (assessDtos.isEmpty()) return@withContext 1
            try {
                val response = tmcNetworkApiService.saveHighRiskAssessData(
                    UserDataDTO(
                        userId = user.userId,
                        entries = assessDtos
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Push to amrit hrp assess data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    updateSyncStatusHrpa(entities)
                                    return@withContext 1
                                } catch (e: Exception) {
                                    Timber.d("HRP Assess entries not synced $e")
                                }

                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                val errorMessage = jsonObj.getString("errorMessage")
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_tb error : $e")
                return@withContext pushUnSyncedRecordsHRPAssess()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_tb error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private suspend fun pushUnSyncedRecordsHRPTrack(): Int {

        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val entities = database.hrpDao.getHRPTrack(SyncState.UNSYNCED)

            val dtos = mutableListOf<Any>()
            entities?.let {
                it.forEach { cache ->
                    dtos.add(cache.toDTO())
                    cache.syncState = SyncState.SYNCED
                }
            }

            if (dtos.isEmpty()) return@withContext 1
            try {
                val response = tmcNetworkApiService.saveHRPTrackData(
                    UserDataDTO(
                        userId = user.userId,
                        entries = dtos
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Push to amrit hrp track data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    updateSyncStatusHrpt(entities)
                                    return@withContext 1
                                } catch (e: Exception) {
                                    Timber.d("HRP Track entries not synced $e")
                                }

                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                val errorMessage = jsonObj.getString("errorMessage")
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, !?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_tb error : $e")
                return@withContext pushUnSyncedRecordsHRPTrack()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_tb error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private suspend fun pushUnSyncedRecordsHRNonPAssess(): Int {

        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val entities = database.hrpDao.getNonPregnantAssess(SyncState.UNSYNCED)

            val dtos = mutableListOf<Any>()
            val ecrDTOs = mutableSetOf<EcrPost>()
            entities?.let {
                it.forEach { cache ->
                    dtos.add(cache.toHighRiskAssessDTO())
                    ecrDTOs.add(mapECR(cache))
                    cache.syncState = SyncState.SYNCED
                }
            }

            val uploadDone = ecrRepo.postECRDataToAmritServer(ecrDTOs)
            if (uploadDone) {
                ecrDTOs.forEach {
                    val ecr = ecrRepo.getSavedRecord(it.benId)
                    ecr?.let { cache ->
                        cache.processed = "P"
                        cache.syncState = SyncState.SYNCED
                        ecrRepo.persistRecord(cache)
                    }
                }
            }
            if (dtos.isEmpty()) return@withContext 1
            try {
                val response = tmcNetworkApiService.saveHighRiskAssessData(
                    UserDataDTO(
                        userId = user.userId,
                        entries = dtos
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Push to amrit non hrp assess data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    updateSyncStatusHrpNonAssess(entities)
                                    return@withContext 1
                                } catch (e: Exception) {
                                    Timber.d("HRP non Assess entries not synced $e")
                                }

                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                val errorMessage = jsonObj.getString("errorMessage")
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, !?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_ hrp error : $e")
                return@withContext pushUnSyncedRecordsHRNonPAssess()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_ hrp error : $e")
                return@withContext -1
            }
            -1
        }
    }


    private suspend fun pushUnSyncedRecordsHRNonPTrack(): Int {

        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val entities = database.hrpDao.getHRNonPTrack(SyncState.UNSYNCED)

            val dtos = mutableListOf<Any>()
            entities?.let {
                it.forEach { cache ->
                    dtos.add(cache.toDTO())
                    cache.syncState = SyncState.SYNCED
                }
            }

            if (dtos.isEmpty()) return@withContext 1
            try {
                val response = tmcNetworkApiService.saveHRNonPTrackData(
                    UserDataDTO(
                        userId = user.userId,
                        entries = dtos
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Push to amrit non hrp track data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    updateSyncStatusHrNonTrack(entities)
                                    return@withContext 1
                                } catch (e: Exception) {
                                    Timber.d("HRP non track entries not synced $e")
                                }

                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                val errorMessage = jsonObj.getString("errorMessage")
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, !?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_ hrp error : $e")
                return@withContext pushUnSyncedRecordsHRNonPTrack()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_ hrp error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun updateSyncStatusHrpt(entities: List<HRPPregnantTrackCache>?) {
        entities?.let {
            entities.forEach {
                database.hrpDao.saveRecord(it)
            }
        }
    }

    private fun updateSyncStatusHrpa(entities: List<HRPPregnantAssessCache>?) {
        entities?.let {
            entities.forEach {
                database.hrpDao.saveRecord(it)
            }
        }
    }

    private fun updateSyncStatusHrpNonAssess(entities: List<HRPNonPregnantAssessCache>?) {
        entities?.let {
            entities.forEach {
                database.hrpDao.saveRecord(it)
            }
        }
    }

    private fun updateSyncStatusHrNonTrack(entities: List<HRPNonPregnantTrackCache>?) {
        entities?.let {
            entities.forEach {
                database.hrpDao.saveRecord(it)
            }
        }
    }

    private suspend fun mapPWR(cache: HRPPregnantAssessCache): PwrPost {
        val pwr: PregnantWomanRegistrationCache? =
            maternalHealthRepo.getSavedRegistrationRecord(cache.benId)

        val pwrPost: PwrPost
        val user = preferenceDao.getLoggedInUser()!!
        if (pwr == null) {
            pwrPost = PwrPost(
                benId = cache.benId,
                createdBy = user.userName,
                createdDate = getCurrentDate(System.currentTimeMillis()),
                updatedBy = user.userName,
                updatedDate = getCurrentDate(System.currentTimeMillis()),
                isFirstPregnancyTest = !cache.multiplePregnancy.equals("Yes"),
                lmpDate = getCurrentDate(cache.lmpDate),
                rhNegative = cache.rhNegative,
                homeDelivery = cache.homeDelivery,
                badObstetric = cache.badObstetric,
                isActive = true,
                isRegistered = false
            )
        } else {
            pwrPost = pwr.asPwrPost()
            pwrPost.isFirstPregnancyTest = !cache.multiplePregnancy.equals("Yes")
            pwrPost.rhNegative = cache.rhNegative
            pwrPost.homeDelivery = cache.homeDelivery
            pwrPost.badObstetric = cache.badObstetric
            pwrPost.lmpDate = getCurrentDate(cache.lmpDate)
            pwrPost.updatedBy = user.userName
            pwrPost.updatedDate = getCurrentDate(System.currentTimeMillis())
        }

        return pwrPost
    }

    private suspend fun mapECR(cache: HRPNonPregnantAssessCache): EcrPost {
        val ecr: EligibleCoupleRegCache? = ecrRepo.getSavedRecord(cache.benId)

        val ecrPost: EcrPost
        val user = preferenceDao.getLoggedInUser()!!

        if (ecr == null) {
            ecrPost = EcrPost(
                benId = cache.benId,
                createdBy = user.userName,
                createdDate = getCurrentDate(System.currentTimeMillis()),
                updatedBy = user.userName,
                updatedDate = getCurrentDate(System.currentTimeMillis()),
                misCarriage = cache.misCarriage,
                homeDelivery = cache.homeDelivery,
                medicalIssues = cache.medicalIssues,
                pastCSection = cache.pastCSection,
                isHighRisk = cache.isHighRisk,
                isRegistered = false
            )
        } else {
            ecrPost = ecr.asPostModel()
            ecrPost.misCarriage = cache.misCarriage
            ecrPost.homeDelivery = cache.homeDelivery
            ecrPost.medicalIssues = cache.medicalIssues
            ecrPost.pastCSection = cache.pastCSection
            ecrPost.isHighRisk = cache.isHighRisk
        }
        return ecrPost
    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        private fun getCurrentDate(millis: Long = System.currentTimeMillis()): String {
            val dateString = dateFormat.format(millis)
            val timeString = timeFormat.format(millis)
            return "${dateString}T${timeString}.000Z"
        }

        private fun getLongFromDate(dateString: String): Long {
            //Jul 22, 2023 8:17:23 AM"
            val f = SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }
}