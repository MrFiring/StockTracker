package ru.mrfiring.stocktracker.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockSearchItem
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetStockSearchHistoryUseCase
import ru.mrfiring.stocktracker.domain.SearchStockSymbolUseCase
import java.io.IOException
import javax.inject.Inject

data class CombinedLoadingState(
    val historyStatus: LoadingStatus?,
    val searchStatus: LoadingStatus?
)

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
    private val searchStockSymbolUseCase: SearchStockSymbolUseCase,
    private val getStockSearchHistoryUseCase: GetStockSearchHistoryUseCase
) : AndroidViewModel(application) {

    private val _status = SingleLiveEvent<CombinedLoadingState>()
    val status: LiveData<CombinedLoadingState>
        get() = _status

    private val _navigateToDetail = SingleLiveEvent<DomainStockSymbol>()
    val navigateToDetail: LiveData<DomainStockSymbol>
        get() = _navigateToDetail

    private val _navigateUp = SingleLiveEvent<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private val _queryFlow = MutableSharedFlow<String>()

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>>
        get() = _history

    private val _searchResults = MutableLiveData<List<DomainStockSearchItem>>()
    val searchResults: LiveData<List<DomainStockSearchItem>>
        get() = _searchResults

    init {
        bindHistory()
        sendSearchRequest()
    }

    fun emitNewQuery(query: String) = viewModelScope.launch {
        _queryFlow.emit(query)
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

    fun rebindHistory() {
        //reset loading states
        _status.value = CombinedLoadingState(
            historyStatus = null,
            searchStatus = null
        )
        bindHistory()
    }

    private fun sendSearchRequest() = viewModelScope.launch {
        _queryFlow
            .debounce(500)
            .filter {
                it.isNotEmpty()
            }
            .onEach {
                _status.value = CombinedLoadingState(
                    searchStatus = LoadingStatus.LOADING,
                    historyStatus = null
                )
            }
            .flatMapConcat {
                flowOf(searchStockSymbolUseCase(it))
            }
            .catch {
                _status.value = CombinedLoadingState(
                    searchStatus = LoadingStatus.ERROR,
                    historyStatus = _status.value?.historyStatus
                )
            }
            .collect {
                _searchResults.value = it
                _status.value = CombinedLoadingState(
                    searchStatus = LoadingStatus.DONE,
                    historyStatus = _status.value?.historyStatus
                )
            }
    }

    fun onNavigateToDetail(item: DomainStockSymbol) {
        _navigateToDetail.value = item
    }

    fun onNavigateUp() {
        _navigateUp.value = true
    }
}