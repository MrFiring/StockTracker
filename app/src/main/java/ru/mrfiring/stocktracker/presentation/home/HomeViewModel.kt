package ru.mrfiring.stocktracker.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetStocksAndQuotesFlowUseCase
import ru.mrfiring.stocktracker.domain.RefreshStocksUseCase
import java.io.IOException
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    application: Application,
    private val getStocksAndQuotesFlowUseCase: GetStocksAndQuotesFlowUseCase,
    private val refreshStocksUseCase: RefreshStocksUseCase
) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _isNetworkError = MutableLiveData<Boolean>()
    val isNetworkError
        get() = _isNetworkError

    private var _stocks = MutableLiveData<List<DomainStockSymbol>>()
    val stocks: LiveData<List<DomainStockSymbol>>
        get() = _stocks

    init {
        refreshDataFromRepository()
    }

    fun navigateToSearch() {
        _navigateToSearchFragment.value = true
    }

    fun navigateToSearchCompleted() {
        _navigateToSearchFragment.value = false
    }

    fun networkErrorShown(){
        _isNetworkError.value = false
    }

    fun refreshDataFromRepository() = viewModelScope.launch {
        try {
            refreshStocksUseCase()
            _stocks =
                getStocksAndQuotesFlowUseCase().asLiveData() as MutableLiveData<List<DomainStockSymbol>>

        }catch (ex: IOException){
            Log.e("refresh", "NETWORK TROUBLE: ${ex.stackTraceToString()}")
            _isNetworkError.value = true
        }
    }


}