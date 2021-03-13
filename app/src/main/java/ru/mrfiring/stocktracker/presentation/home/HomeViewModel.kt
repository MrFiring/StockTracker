package ru.mrfiring.stocktracker.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _isNetworkError = MutableLiveData<Boolean>()
    val isNetworkError
    get() = _isNetworkError

    init{
        refreshDataFromRepository()
    }

    fun navigateToSearch(){
        _navigateToSearchFragment.value = true
    }

    fun navigateToSearchCompleted(){
        _navigateToSearchFragment.value = false
    }

    fun networkErrorShown(){
        _isNetworkError.value = false
    }

    fun refreshDataFromRepository() = viewModelScope.launch {
        try {
        }catch (ex: IOException){
            Log.e("refresh", "NETWORK TROUBLE: ${ex.stackTraceToString()}")
            _isNetworkError.value = true
        }
    }


}