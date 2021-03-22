package ru.mrfiring.stocktracker.presentation.details.general

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.FetchCompanyUseCase
import ru.mrfiring.stocktracker.domain.GetCompanyBySymbolUseCase
import java.io.IOException
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, ERROR, DONE
}

@HiltViewModel
class DetailsGeneralViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val getCompanyBySymbolUseCase: GetCompanyBySymbolUseCase,
    private val fetchCompanyUseCase: FetchCompanyUseCase
) : AndroidViewModel(application) {

    private val symbol = savedStateHandle.get<String>("symbol") ?: ""

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _company = MutableLiveData<DomainCompany>()
    val company: LiveData<DomainCompany>
        get() = _company


    init {
        bindCompany()
    }

    private fun bindCompany() = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            fetchCompanyUseCase(symbol)
            val companyInfo = getCompanyBySymbolUseCase(symbol)
            companyInfo?.let {
                _company.value = it
                _status.value = LoadingStatus.DONE
            }
        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
        }
    }

}