package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.PMSMACache

@Dao
interface PmsmaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg pmsmaCache: PMSMACache)

    @Query("SELECT * FROM PMSMA WHERE processed = 'N'")
    suspend fun getAllUnprocessedPmsma(): List<PMSMACache>

    @Query("select count(*) from PMSMA")
    suspend fun pmsmaCount(): Int

    @Query("SELECT * FROM PMSMA WHERE benId =:benId and isActive = 1 LIMIT 1")
    suspend fun getPmsma(benId: Long): PMSMACache?

    @Query("SELECT * FROM PMSMA WHERE benId IN(:benId) and isActive = 1")
    suspend fun getAllPmsma(benId: Set<Long>): List<PMSMACache>

    @Update
    abstract fun updatePmsmaRecord(it: PMSMACache)

}