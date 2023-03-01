package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.MDSRCache

@Dao
interface MdsrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg mdsrCache: MDSRCache)

    @Query("SELECT * FROM MDSR WHERE processed = 'N'")
    suspend fun getAllUnprocessedMdsr(): List<MDSRCache>

    @Query("select count(*) from MDSR")
    suspend fun mdsrCount(): Int

    @Update
    suspend fun updateMdsrRecord(it: MDSRCache)
}