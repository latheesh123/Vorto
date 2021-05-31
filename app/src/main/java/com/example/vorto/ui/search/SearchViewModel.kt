package com.example.vorto.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vorto.model.BusinessResponse
import com.example.vorto.model.Businesses
import com.example.vorto.model.ServiceResult
import com.example.vorto.repo.Repository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private var _dialog = MutableLiveData<String>()
    var dialog: LiveData<String> = _dialog

    private var _businesslist = MutableLiveData<ArrayList<Businesses>>()
    var businesslist: LiveData<ArrayList<Businesses>> = _businesslist

    fun getBusinessList(query: String,latitude:Double,longitude:Double) {
        viewModelScope.launch {
            val itemResult = repository.getBusinessContacts(query,latitude,longitude)
            if (itemResult is ServiceResult.Error) {
                _dialog.value = itemResult.error.errorMessages
            } else {
                val itemData =
                    ((itemResult as? ServiceResult.Success)?.data as? BusinessResponse)?.businesses
                _businesslist.value = itemData as ArrayList<Businesses>

            }
        }
    }

}