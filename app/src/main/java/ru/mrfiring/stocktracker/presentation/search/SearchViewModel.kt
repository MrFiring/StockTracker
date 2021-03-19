package ru.mrfiring.stocktracker.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.DomainStockSearchItem
import ru.mrfiring.stocktracker.domain.GetStockSearchHistoryUseCase
import ru.mrfiring.stocktracker.domain.SearchStockSymbolUseCase
import ru.mrfiring.stocktracker.presentation.details.LoadingStatus
import java.io.IOException
import javax.inject.Inject

data class CombinedLoadingState(
    val historyStatus: LoadingStatus?,
    val searchStatus: LoadingStatus?
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
    private val searchStockSymbolUseCase: SearchStockSymbolUseCase,
    private val getStockSearchHistoryUseCase: GetStockSearchHistoryUseCase
) : AndroidViewModel(application) {

    private val _status = MutableLiveData<CombinedLoadingState>()
    val status: LiveData<CombinedLoadingState>
        get() = _status

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>>
        get() = _history

    private val _searchResults = MutableLiveData<List<DomainStockSearchItem>>()
    val searchResults: LiveData<List<DomainStockSearchItem>>
        get() = _searchResults

    init {
        bindHistory()
    }

    private fun bindHistory() = viewModelScope.launch {
        try {
            _status.value = CombinedLoadingState(
                historyStatus = LoadingStatus.LOADING,
                searchStatus = _status.value?.searchStatus
            )
            _history.value = getStockSearchHistoryUseCase()
            _status.value = CombinedLoadingState(
                historyStatus = LoadingStatus.DONE,
                searchStatus = _status.value?.searchStatus
            )
        } catch (ex: IOException) {
            _status.value = CombinedLoadingState(
                historyStatus = LoadingStatus.ERROR,
                searchStatus = _status.value?.searchStatus
            )
        }
    }

    fun sendSearchRequest(query: String) = viewModelScope.launch {
        try {
            _status.value = CombinedLoadingState(
                searchStatus = LoadingStatus.LOADING,
                historyStatus = _status.value?.historyStatus
            )
            _searchResults.value = searchStockSymbolUseCase(query)
            _status.value = CombinedLoadingState(
                searchStatus = LoadingStatus.DONE,
                historyStatus = _status.value?.historyStatus
            )
        } catch (ex: IOException) {
            _status.value = CombinedLoadingState(
                searchStatus = LoadingStatus.ERROR,
                historyStatus = _status.value?.historyStatus
            )
        }
    }
}