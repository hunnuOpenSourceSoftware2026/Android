package com.example.inspectionlab4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InspectionRecord::class], version = 1, exportSchema = false)
abstract class InspectionDatabase : RoomDatabase() {
    abstract fun recordDao(): InspectionRecordDao

    companion object {
        private var instance: InspectionDatabase? = null

        @Synchronized
        fun get(context: Context): InspectionDatabase =
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                InspectionDatabase::class.java,
                "inspection.db",
            ).build().also { instance = it }
    }
}

