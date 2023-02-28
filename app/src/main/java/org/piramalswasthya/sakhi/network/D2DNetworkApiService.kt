package org.piramalswasthya.sakhi.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.piramalswasthya.sakhi.model.CDRPost
import org.piramalswasthya.sakhi.model.MdsrPost
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface D2DNetworkApiService {

    @Headers("No-Auth: true")
    @POST("user/authenticate")
    suspend fun getJwtToken(
        @Body userInput: D2DAuthUserRequest,
    ): D2DAuthUserResponse

    @POST("saveuser")
    suspend fun saveUserD2d(
        @Body userInput: D2DSaveUserRequest
    ): D2DSaveUserResponse

    @GET("VillageData/getVillageDetailsByAshaId")
    suspend fun getVillageData(
        @Query("ashaId") userId : Int
    ): Response<ResponseBody>


    @POST("mdsrRegister")
    @Headers("Content-Type: application/json")
    suspend fun postMdsrDataRegister(
        @Body mdsrPostList: List<MdsrPost>
    ): Response<ResponseBody>

    @POST("cdrRegister")
    @Headers("Content-Type: application/json")
    suspend fun postCdrRegister(
        @Body cdrPost: List<CDRPost>
    ): Response<ResponseBody>

}