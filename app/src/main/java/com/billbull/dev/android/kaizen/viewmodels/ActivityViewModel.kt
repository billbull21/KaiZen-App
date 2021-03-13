package com.billbull.dev.android.kaizen.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase

class ActivityViewModel(val db: AppDatabase): ViewModel() {

    suspend fun insertActivity(data: ActivityModel): Long = db.activityDao().insert(data)

    suspend fun updateActivity(data: ActivityModel) = db.activityDao().update(data)

    suspend fun deleteActivityById(id: Int) = db.activityDao().delete(id)

    suspend fun clearActivity() = db.activityDao().deleteAll()

    fun getAllActivity(): LiveData<List<ActivityModel>> = db.activityDao().fetchAllActivity()

}