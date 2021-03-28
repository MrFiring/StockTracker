package ru.mrfiring.stocktracker.presentation.details.chart

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.DomainStockCandles
import ru.mrfiring.stocktracker.domain.FetchStockCandlesUseCase
import ru.mrfiring.stocktracker.domain.GetStockCandlesUseCase
import ru.mrfiring.stocktracker.presentation.details.general.LoadingStatus
import java.io.IOException
import javax.inject.Inject

enum class CandlesResolution(val value: String) {
    ONE_MIN("1"),
    FIVE_MIN("5"),
    SIXTY_MIN("30"),
    DAY("D"),
}

@HiltViewModel
class ChartViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val fetchStockCandlesUseCase: FetchStockCandlesUseCase,
    private val getStockCandlesUseCase: GetStockCandlesUseCase
) : AndroidViewModel(application) {

    private val symbol = savedStateHandle.get<String>("symbol") ?: ""

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private var _candles = MutableLiveData<DomainStockCandles>()
    val candles: LiveData<DomainStockCandles>
        get() = _candles

    private var _resolution = SingleLiveEvent<CandlesResolution>()
    val resolution: LiveData<CandlesResolution>
        get() = _resolution

    @SuppressLint("NullSafeMutableLiveData")
    fun bindData(resolution: CandlesResolution) = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING

            val curDt = DateTime.now()
            val curUTCTime: Long = curDt.millis / 1000L
            val resTime: Long = curDt.minusYears(1).millis / 1000L
            fetchStockCandlesUseCase(
                symbol = symbol,
                resolution = resolution.value,
                fromTime = resTime,
                toTime = curUTCTime
            )

            _candles.value = getStockCandlesUseCase(symbol, resolution.value)
            _resolution.value = resolution

            _status.value = LoadingStatus.DONE
        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
            Log.e("Chart", ex.stackTraceToString())
        }
    }

}