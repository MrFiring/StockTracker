package ru.mrfiring.stocktracker.presentation.home.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.GetFavoriteListUseCase
import ru.mrfiring.stocktracker.domain.UpdateStockSymbolUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    application: Application,
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    private val updateStockSymbolUseCase: UpdateStockSymbolUseCase
) : AndroidViewModel(application) {

    private val _navigateToDetailFragment = SingleLiveEvent<DomainStockSymbol>()
    val navigateToDetailFragment: LiveData<DomainStockSymbol>
        get() = _navigateToDetailFragment

    private val _favorites = MutableLiveData<List<DomainStockSymbol>>()
    val favorites: LiveData<List<DomainStockSymbol>>
        get() = _favorites

    init {
        bindData()
    }

    private fun bindData() = viewModelScope.launch {
        _favorites.value = getFavoriteListUseCase()
    }

    fun updateSymbol(symbol: DomainStockSymbol) = viewModelScope.launch {
        val newSymbol = DomainStockSymbol(
            symbol.symbol,
            symbol.companyName,
            symbol.logoUrl,
            symbol.quote,
            !symbol.isFavorite
        )
        updateStockSymbolUseCase(newSymbol)
        bindData()
    }

    fun navigateToDetail(symbol: DomainStockSymbol) {
        _navigateToDetailFragment.value = symbol
    }
}