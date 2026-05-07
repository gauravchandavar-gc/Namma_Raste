package com.example.nammarastereporter.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT * FROM reports WHERE ticketId = :ticketId LIMIT 1")
    fun getReportByTicketId(ticketId: String): LiveData<ReportEntity?>

    @Query("SELECT * FROM reports ORDER BY id DESC")
    fun getAllReports(): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE ticketId = :ticketId LIMIT 1")
    suspend fun getReportByTicketIdSync(ticketId: String): ReportEntity?
}
