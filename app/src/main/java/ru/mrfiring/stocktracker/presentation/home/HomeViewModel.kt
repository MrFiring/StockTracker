package ru.mrfiring.stocktracker.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetStocksAndQuotesFlowUseCase
import ru.mrfiring.stocktracker.domain.RefreshQuotesUseCase
import ru.mrfiring.stocktracker.domain.RefreshStocksUseCase
import java.io.IOException
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    application: Application,
    private val getStocksAndQuotesFlowUseCase: GetStocksAndQuotesFlowUseCase,
    private val refreshStocksUseCase: RefreshStocksUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase
) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = SingleLiveEvent<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _isNetworkError = SingleLiveEvent<Boolean>()
    val isNetworkError
        get() = _isNetworkError

    private var _stocks: LiveData<List<DomainStockSymbol>> =
        getStocksAndQuotesFlowUseCase().asLiveData(viewModelScope.coroutineContext)
    val stocks: LiveData<List<DomainStockSymbol>>
        get() = _stocks

    init {
        refreshStocks()
    }

    fun navigateToSearch() {
        _navigateToSearchFragment.value = true
    }

    private fun refreshStocks() = viewModelScope.launch {
        try {
            refreshStocksUseCase()
            refreshQuotesUseCase()
        } catch (ex: IOException) {
            Log.e("refresh", "NETWORK TROUBLE: ${ex.stackTraceToString()}")
            _isNetworkError.value = true
        }
    }


}