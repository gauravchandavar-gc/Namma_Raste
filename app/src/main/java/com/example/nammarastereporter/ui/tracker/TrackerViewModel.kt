package com.example.nammarastereporter.ui.tracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nammarastereporter.NammaRasteApp
import com.example.nammarastereporter.data.db.ReportEntity
import kotlinx.coroutines.launch

class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as NammaRasteApp).repository

    private val _searchResult = MutableLiveData<ReportEntity?>()
    val searchResult: LiveData<ReportEntity?> = _searchResult

    val allReports: LiveData<List<ReportEntity>> = repository.allReports

    private val _notFound = MutableLiveData<Boolean>()
    val notFound: LiveData<Boolean> = _notFound

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun searchByTicketId(ticketId: String) {
        _isSearching.value = true
        _notFound.value = false
        _searchResult.value = null

        viewModelScope.launch {
            val report = repository.getReportByTicketIdSync(ticketId.trim().uppercase())
            if (report != null) {
                _searchResult.postValue(report)
                _notFound.postValue(false)
            } else {
                _searchResult.postValue(null)
                _notFound.postValue(true)
            }
            _isSearching.postValue(false)
        }
    }

    fun clearSearch() {
        _searchResult.value = null
        _notFound.value = false
    }
}
