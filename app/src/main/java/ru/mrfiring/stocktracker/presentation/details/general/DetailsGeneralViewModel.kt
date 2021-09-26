package ru.mrfiring.stocktracker.presentation.details.general

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.domain.FetchCompanyUseCase
import ru.mrfiring.stocktracker.domain.GetCompanyBySymbolUseCase
import ru.mrfiring.stocktracker.domain.GetStockAndQuoteBySymbolUseCase
import ru.mrfiring.stocktracker.domain.UpdateStockSymbolUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsGeneralViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCompanyBySymbolUseCase: GetCompanyBySymbolUseCase,
    private val fetchCompanyUseCase: FetchCompanyUseCase,
    private val getStockAndQuoteBySymbolUseCase: GetStockAndQuoteBySymbolUseCase,
    private val updateStockSymbolUseCase: UpdateStockSymbolUseCase
) : ViewModel() {

    private val symbol = savedStateHandle.get<String>("symbol").orEmpty()

    private val _state = MutableLiveData<DetailsGeneralFragmentState>()
    val state: LiveData<DetailsGeneralFragmentState> = _state

    init {
        bindData()
    }

    private fun bindData() = viewModelScope.launch {
        try {
            _state.value = DetailsGeneralFragmentState.Loading
            fetchCompanyUseCase(symbol)

            launch {
                getCompanyBySymbolUseCase(symbol)?.let { company ->
                    _state.value?.let {
                        if (it is DetailsGeneralFragmentState.Content) {
                            _state.value = it.copy(company = company)
                        } else {
                            _state.value = DetailsGeneralFragmentState.Content(
                                company = company,
                                stockAndQuote = null
                            )
                        }
                    }
                }
            }
            launch {
                getStockAndQuoteBySymbolUseCase(symbol)?.let { stock ->
                    _state.value?.let {
                        if (it is DetailsGeneralFragmentState.Content) {
                            _state.value = it.copy(stockAndQuote = stock)
                        } else {
                            _state.value = DetailsGeneralFragmentState.Content(
                                company = null,
                                stockAndQuote = stock
                            )
                        }
                    }
                }
            }

        } catch (ex: IOException) {
            _state.value = DetailsGeneralFragmentState.Error
        }
    }

    fun retry() = bindData()

    fun markAsFavorite() = viewModelScope.launch {
        val curState = _state.value as DetailsGeneralFragmentState.Content
        curState.stockAndQuote

        val newStock = curState.stockAndQuote?.let { it.copy(isFavorite = !it.isFavorite) }
        if (newStock != null) {
            updateStockSymbolUseCase(newStock)
            _state.value = curState.copy(stockAndQuote = newStock)
        }
    }


}