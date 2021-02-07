package com.billbull.dev.android.kaizen.models.db.dao

import androidx.room.*
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel

@Dao
interface ActivityDao {

    @Insert
    suspend fun insert(uangMasuk: ActivityModel)

    @Update
    suspend fun update(uangMasuk: ActivityModel)

    @Query("DELETE FROM t_activity WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM t_activity")
    suspend fun deleteAll()

    @Query("SELECT * FROM t_activity ORDER BY id DESC")
    suspend fun fetchAllActivity(): List<ActivityModel>

}