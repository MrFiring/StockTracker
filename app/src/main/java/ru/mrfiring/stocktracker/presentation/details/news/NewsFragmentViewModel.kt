package ru.mrfiring.stocktracker.presentation.details.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainCompanyNews
import ru.mrfiring.stocktracker.domain.FetchCompanyNewsListUseCase
import ru.mrfiring.stocktracker.domain.GetCompanyNewsListBySymbolUseCase
import java.io.IOException
import javax.inject.Inject

private const val TIME_FORMAT = "YYYY-MM-dd"

@HiltViewModel
class NewsFragmentViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val getCompanyNewsListBySymbolUseCase: GetCompanyNewsListBySymbolUseCase,
    private val fetchCompanyNewsListUseCase: FetchCompanyNewsListUseCase
) : AndroidViewModel(application) {

    private val symbol: String = savedStateHandle.get<String>("symbol") ?: ""

    private val _state = MutableLiveData<NewsFragmentState>()
    val state: LiveData<NewsFragmentState> = _state

    private val _openWebpage = SingleLiveEvent<DomainCompanyNews>()
    val openWebpage: LiveData<DomainCompanyNews>
        get() = _openWebpage

    init {
        bindData()
    }

    private fun bindData() = viewModelScope.launch {
        try {
            val curTime = DateTime.now()
            _state.value = NewsFragmentState.Loading
            fetchCompanyNewsListUseCase(
                symbol,
                fromDate = curTime.minusDays(3).toString(TIME_FORMAT),
                toDate = curTime.toString(TIME_FORMAT)
            )
            _state.value = NewsFragmentState.Content(getCompanyNewsListBySymbolUseCase(symbol))
        } catch (ex: IOException) {
            Log.e("NEWS", ex.toString())
            _state.value = NewsFragmentState.Error
        }
    }

    fun retry() = bindData()

    fun openSourceWebsite(item: DomainCompanyNews) {
        _openWebpage.value = item
    }
}