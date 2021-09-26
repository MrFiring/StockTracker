package ru.mrfiring.stocktracker.presentation.home.stocks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.mrfiring.stocktracker.EmptyLiveData
import ru.mrfiring.stocktracker.LiveEvent
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetStocksAndQuotesLiveDataCase
import ru.mrfiring.stocktracker.domain.RefreshQuotesUseCase
import ru.mrfiring.stocktracker.domain.UpdateStockSymbolUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    getStocksAndQuotesLiveDataCase: GetStocksAndQuotesLiveDataCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase,
    private val updateStockSymbolUseCase: UpdateStockSymbolUseCase
) : ViewModel() {

    private val _navigateToDetailFragment = SingleLiveEvent<String>()
    val navigateToDetailFragment: LiveData<String>
        get() = _navigateToDetailFragment

    private val _isNetworkLoadingError = LiveEvent()
    val isNetworkLoadingError: EmptyLiveData
        get() = _isNetworkLoadingError

    private val _isRefreshError = LiveEvent()
    val isRefreshError: EmptyLiveData
        get() = _isRefreshError

    private val _coldStartIndicator = MutableLiveData<Boolean>()
    val coldStartIndicator: LiveData<Boolean>
        get() = _coldStartIndicator

    private var _stocks = MutableLiveData<PagingData<DomainStockSymbol>>()
    val stocks: LiveData<PagingData<DomainStockSymbol>>
        get() = _stocks

    init {
        _stocks = getStocksAndQuotesLiveDataCase()
            .cachedIn(viewModelScope) as MutableLiveData<PagingData<DomainStockSymbol>>

        refreshQuotes()
    }

    fun navigateToDetail(symbol: String) {
        _navigateToDetailFragment.value = symbol
    }

    fun markAsFavorite(symbol: DomainStockSymbol) = viewModelScope.launch {
        val newSymbol = DomainStockSymbol(
            symbol.symbol,
            symbol.companyName,
            symbol.logoUrl,
            symbol.quote,
            !symbol.isFavorite
        )
        updateStockSymbolUseCase(newSymbol)
    }

    private fun refreshQuotes() = viewModelScope.launch {
        try {
            refreshQuotesUseCase()
        } catch (ex: IOException) {
            Log.e("refresh", "NETWORK TROUBLE: ${ex.stackTraceToString()}")
            _isRefreshError()
        } catch (ex: HttpException) {
            Log.e("refresh", "HTTP exception")
            _isRefreshError()
        }
    }

    fun onColdStart() {
        if (_coldStartIndicator.value != null) {
            return
        }
        _coldStartIndicator.value = true
    }

    fun coldQuotesUpdate(coldFlag: Boolean) {
        if (coldFlag) {
            refreshQuotes()
            _coldStartIndicator.value = false
        }
    }

    fun retryRefreshQuotes() = refreshQuotes()

    fun onLoadingNetworkError() {
        _isNetworkLoadingError()
    }

}