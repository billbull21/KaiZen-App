package com.billbull.dev.android.kaizen.models.db.init

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.billbull.dev.android.kaizen.models.db.dao.ActivityDao
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel


@Database(entities = [ActivityModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, AppDatabase::class.java,
                    "app_database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

        // add column on table without delete data
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE t_uang_masuk ADD COLUMN tanggal TEXT"
//                )
//                database.execSQL(
//                    "ALTER TABLE t_uang_masuk ADD COLUMN nomor TEXT"
//                )
            }
        }
    }
}