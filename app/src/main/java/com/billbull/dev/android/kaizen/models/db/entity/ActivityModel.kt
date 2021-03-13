package com.billbull.dev.android.kaizen.models.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "t_activity",
    indices = [
        Index(value = ["activity_name"], unique = true),
        Index(value = ["activity_time"], unique = true)
    ]
)

@Parcelize
data class ActivityModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val activity_name: String,
    val activity_time: String,
    @ColumnInfo(defaultValue = "0") // 0 is false
    val is_remind_active: Boolean
) : Parcelable