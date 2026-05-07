package com.example.nammarastereporter.data.repository

import androidx.lifecycle.LiveData
import com.example.nammarastereporter.data.db.ReportDao
import com.example.nammarastereporter.data.db.ReportEntity

class ReportRepository(private val reportDao: ReportDao) {

    val allReports: LiveData<List<ReportEntity>> = reportDao.getAllReports()

    suspend fun insertReport(report: ReportEntity) {
        reportDao.insertReport(report)
    }

    fun getReportByTicketId(ticketId: String): LiveData<ReportEntity?> {
        return reportDao.getReportByTicketId(ticketId)
    }

    suspend fun getReportByTicketIdSync(ticketId: String): ReportEntity? {
        return reportDao.getReportByTicketIdSync(ticketId)
    }
}
