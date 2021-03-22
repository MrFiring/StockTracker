package ru.mrfiring.stocktracker.presentation.details.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.mrfiring.stocktracker.domain.DomainCompanyNews
import ru.mrfiring.stocktracker.domain.FetchCompanyNewsListUseCase
import ru.mrfiring.stocktracker.domain.GetCompanyNewsListBySymbolUseCase
import ru.mrfiring.stocktracker.presentation.details.general.LoadingStatus
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

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _news = MutableLiveData<List<DomainCompanyNews>>()
    val news: LiveData<List<DomainCompanyNews>>
        get() = _news


    init {
        bindData()
    }

    fun bindData() = viewModelScope.launch {
        try {
            val curTime = DateTime.now()
            _status.value = LoadingStatus.LOADING
            fetchCompanyNewsListUseCase(
                symbol,
                fromDate = curTime.minusDays(3).toString(TIME_FORMAT),
                toDate = curTime.toString(TIME_FORMAT)
            )
            _news.value = getCompanyNewsListBySymbolUseCase(symbol)
            _status.value = LoadingStatus.DONE
        } catch (ex: IOException) {
            Log.e("NEWS", ex.toString())
            _status.value = LoadingStatus.ERROR
        }
    }

}