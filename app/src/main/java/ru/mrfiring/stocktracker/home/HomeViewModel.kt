package ru.mrfiring.stocktracker.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mrfiring.stocktracker.repository.StockRepository
import ru.mrfiring.stocktracker.repository.database.asDomainObject
import ru.mrfiring.stocktracker.repository.database.getDatabase
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.repository.network.FinhubNetwork
import ru.mrfiring.stocktracker.repository.network.StockSymbol

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment


    private val stockRepository = StockRepository(getDatabase(application))
    val stockList = stockRepository.symbols

    init{
        refreshDataFromRepository()
    }

    fun navigateToSearch(){
        _navigateToSearchFragment.value = true
    }

    fun navigateToSearchCompleted(){
        _navigateToSearchFragment.value = false
    }

    fun refreshDataFromRepository() = viewModelScope.launch {
        stockRepository.refreshStocks()
    }


}