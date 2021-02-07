package com.billbull.dev.android.kaizen.models.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "t_activity",
    indices = [
        Index(value = ["activity_name"], unique = true),
        Index(value = ["activity_time"], unique = true)
    ]
)
data class ActivityModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val activity_name: String,
    val activity_time: String
)