package ru.mrfiring.stocktracker.presentation.details.general

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.*
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
    private val fetchCompanyUseCase: FetchCompanyUseCase,
    private val getStockAndQuoteBySymbolUseCase: GetStockAndQuoteBySymbolUseCase,
    private val updateStockSymbolUseCase: UpdateStockSymbolUseCase
) : AndroidViewModel(application) {

    private val symbol = savedStateHandle.get<String>("symbol") ?: ""

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _company = MutableLiveData<DomainCompany>()
    val company: LiveData<DomainCompany>
        get() = _company

    private val _stock = MutableLiveData<DomainStockSymbol>()
    val stock: LiveData<DomainStockSymbol>
        get() = _stock

    init {
        bindData()
    }

    private fun bindData() = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            fetchCompanyUseCase(symbol)
            val companyInfo = getCompanyBySymbolUseCase(symbol)
            companyInfo?.let {
                _company.value = it
                _stock.value = getStockAndQuoteBySymbolUseCase(symbol)
                _status.value = LoadingStatus.DONE
            }
        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
        }
    }

    fun retry() = bindData()

    fun markAsFavorite() = viewModelScope.launch {
        val curStock = _stock.value
        curStock?.let {
            val newStock = DomainStockSymbol(
                it.symbol, it.companyName, it.logoUrl, it.quote, !it.isFavorite
            )
            _stock.value = newStock
            updateStockSymbolUseCase(newStock)
        }
    }

}