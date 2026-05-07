package com.example.nammarastereporter.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nammarastereporter.NammaRasteApp
import com.example.nammarastereporter.data.db.ReportEntity
import com.example.nammarastereporter.utils.TicketIdGenerator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as NammaRasteApp).repository

    private val _reportSaved = MutableLiveData<String?>()
    val reportSaved: LiveData<String?> = _reportSaved

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude

    fun setLocation(lat: Double, lng: Double) {
        _latitude.value = lat
        _longitude.value = lng
    }

    fun submitReport(issueType: String, imagePath: String, userName: String, userPhone: String) {
        val ticketId = TicketIdGenerator.generate()
        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            .format(Date())

        val report = ReportEntity(
            ticketId = ticketId,
            issueType = issueType,
            latitude = _latitude.value ?: 0.0,
            longitude = _longitude.value ?: 0.0,
            timestamp = timestamp,
            imagePath = imagePath,
            userName = userName,
            userPhone = userPhone,
            status = "Pending"
        )

        viewModelScope.launch {
            repository.insertReport(report)
            _reportSaved.postValue(ticketId)
        }
    }

    fun clearSavedState() {
        _reportSaved.value = null
    }
}
