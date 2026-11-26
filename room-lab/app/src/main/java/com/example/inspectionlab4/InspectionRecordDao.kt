package com.example.inspectionlab4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionRecordDao {
    @Insert
    suspend fun insert(record: InspectionRecord)

    @Query("SELECT * FROM inspection_records ORDER BY recordedAt DESC, id DESC")
    fun observeAll(): Flow<List<InspectionRecord>>
}

