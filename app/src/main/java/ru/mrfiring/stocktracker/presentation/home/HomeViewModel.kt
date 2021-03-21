package ru.mrfiring.stocktracker.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetStocksAndQuotesLiveDataCase
import ru.mrfiring.stocktracker.domain.RefreshQuotesUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val getStocksAndQuotesLiveDataCase: GetStocksAndQuotesLiveDataCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase
) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = SingleLiveEvent<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _navigateToDetailFragment = SingleLiveEvent<String>()
    val navigateToDetailFragment: LiveData<String>
        get() = _navigateToDetailFragment

    private val _isNetworkError = SingleLiveEvent<Boolean>()
    val isNetworkError
        get() = _isNetworkError

    private var _stocks = MutableLiveData<PagingData<DomainStockSymbol>>()
    val stocks: LiveData<PagingData<DomainStockSymbol>>
        get() = _stocks

    init {
        _stocks = getStocksAndQuotesLiveDataCase()
            .cachedIn(viewModelScope) as MutableLiveData<PagingData<DomainStockSymbol>>
        refreshStocks()
    }

    fun navigateToSearch() {
        _navigateToSearchFragment.value = true
    }

    fun navigateToDetail(symbol: String) {
        _navigateToDetailFragment.value = symbol
    }

    private fun refreshStocks() = viewModelScope.launch {
        try {
            refreshQuotesUseCase()
        } catch (ex: IOException) {
            Log.e("refresh", "NETWORK TROUBLE: ${ex.stackTraceToString()}")
            _isNetworkError.value = true
        }
    }


}