package com.billbull.dev.android.kaizen.models.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(uangMasuk: ActivityModel): Long

    @Update
    suspend fun update(uangMasuk: ActivityModel)

    @Query("DELETE FROM t_activity WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM t_activity")
    suspend fun deleteAll()

    // why not use suspend ? because Room does not support LiveData with suspended functions.
    // LiveData already works on a background thread and should be used directly without using coroutines
    @Query("SELECT * FROM t_activity ORDER BY id DESC")
    fun fetchAllActivity(): LiveData<List<ActivityModel>>

}