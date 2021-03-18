package ru.mrfiring.stocktracker.presentation.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.DomainCompany
import java.io.IOException
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, ERROR, DONE
}

class DetailsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _company = MutableLiveData<DomainCompany>()
    val company: LiveData<DomainCompany>
        get() = _company


    fun bindCompany() = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            //to task
            _status.value = LoadingStatus.DONE
        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
        }
    }

}