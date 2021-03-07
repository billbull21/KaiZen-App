package com.billbull.dev.android.kaizen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase

class ActivityViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java))
            return ActivityViewModel(db) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}