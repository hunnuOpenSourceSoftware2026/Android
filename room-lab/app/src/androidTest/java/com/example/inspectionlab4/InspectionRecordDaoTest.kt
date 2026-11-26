package com.example.inspectionlab4

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InspectionRecordDaoTest {
    @Test
    fun repeatedTaskCreatesTwoRowsAndSurvivesReopening() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val filename = "inspection-test-${System.nanoTime()}.db"
        fun openDatabase() =
            Room.databaseBuilder(context, InspectionDatabase::class.java, filename).build()

        try {
            val firstDb = openDatabase()
            val expected = try {
                val dao = firstDb.recordDao()
                assertEquals(emptyList<InspectionRecord>(), dao.observeAll().first())
                val row = InspectionRecord(
                    taskId = "A203-DEMO",
                    deviceName = "投影仪",
                    location = "A203",
                    inspectorName = "Chen",
                    recordedAt = 100,
                )
                dao.insert(row)
                dao.insert(row.copy(recordedAt = 200))
                dao.observeAll().first().also { records ->
                    assertEquals(listOf(200L, 100L), records.map { it.recordedAt })
                    assertEquals(listOf(row.taskId, row.taskId), records.map { it.taskId })
                    assertNotEquals(records[0].id, records[1].id)
                }
            } finally {
                firstDb.close()
            }
            val reopened = openDatabase()
            try {
                assertEquals(expected, reopened.recordDao().observeAll().first())
            } finally {
                reopened.close()
            }
        } finally {
            context.deleteDatabase(filename)
        }
    }
}

