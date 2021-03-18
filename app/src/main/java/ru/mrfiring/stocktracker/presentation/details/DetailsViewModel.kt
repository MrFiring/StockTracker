package ru.mrfiring.stocktracker.presentation.details

import android.app.Application
import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.FetchCompanyUseCase
import ru.mrfiring.stocktracker.domain.GetCompanyBySymbolUseCase
import java.io.IOException

enum class LoadingStatus {
    LOADING, ERROR, DONE
}


class DetailsViewModel @AssistedInject constructor(
    application: Application,
    @Assisted private var symbol: String,
    private val getCompanyBySymbolUseCase: GetCompanyBySymbolUseCase,
    private val fetchCompanyUseCase: FetchCompanyUseCase
) : AndroidViewModel(application) {

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


    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(symbol: String): DetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            symbol: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(symbol) as T
            }

        }
    }

}