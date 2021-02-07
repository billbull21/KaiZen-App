package com.billbull.dev.android.kaizen.viewmodels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import com.billbull.dev.android.kaizen.models.db.dao.ActivityDao
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityViewModel(context: Context): ViewModel() {

    private var dao: ActivityDao

    init {
        val db = AppDatabase.getInstance(context)
        dao = db.activityDao()
    }

    fun insertActivity(data: ActivityModel) {
        GlobalScope.launch(IO) {
            try {
                dao.insert(data)
            } catch (e: SQLiteConstraintException) {
                Log.e("ERROR : ", "${e.message}")
                Log.e("ERROR : ", "$e")
            }
        }
    }

    suspend fun updateActivity(data: ActivityModel) = dao.update(data)

    suspend fun deleteActivityById(id: Int) = dao.delete(id)

    suspend fun clearActivity() = dao.deleteAll()

    suspend fun getAllActivity() = dao.fetchAllActivity()

}