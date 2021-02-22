package ru.mrfiring.stocktracker.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mrfiring.stocktracker.repository.network.FinhubNetwork
import ru.mrfiring.stocktracker.repository.network.StockSymbol

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    val _response = MutableLiveData<List<StockSymbol>>()

    init{
        getFinhubSymbols()
    }

    fun navigateToSearch(){
        _navigateToSearchFragment.value = true
    }

    fun navigateToSearchCompleted(){
        _navigateToSearchFragment.value = false
    }

    fun getFinhubSymbols(){
        FinhubNetwork.finhub.getStockSymbols("US","c0lvlln48v6p8fvj0ceg").enqueue(object : Callback<List<StockSymbol>> {
            override fun onResponse(
                    call: Call<List<StockSymbol>>,
                    response: Response<List<StockSymbol>>
            ) {
                _response.value = response.body()
            }

            override fun onFailure(call: Call<List<StockSymbol>>, t: Throwable) {
                _response.value = null
            }
        })
    }


}