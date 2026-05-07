package com.example.nammarastereporter

import android.app.Application
import com.example.nammarastereporter.data.db.AppDatabase
import com.example.nammarastereporter.data.repository.ReportRepository

class NammaRasteApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: ReportRepository by lazy { ReportRepository(database.reportDao()) }
}
