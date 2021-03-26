package ru.mrfiring.stocktracker.presentation.details.chart

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.mrfiring.stocktracker.domain.DomainStockCandles
import ru.mrfiring.stocktracker.domain.FetchStockCandlesUseCase
import ru.mrfiring.stocktracker.domain.GetStockCandlesFlowUseCase
import ru.mrfiring.stocktracker.presentation.details.general.LoadingStatus
import java.io.IOException
import javax.inject.Inject

enum class CandlesResolution(val value: String) {
    ONE_MIN("1"), FIVE_MIN("5"), FIFTEEN_MIN("15"),
    THIRTY_MIN("30"), SIXTY_MIN("60"), ONE_DAY("D"),
    ONE_WEEK("W"), ONE_MONTH("M")
}

@HiltViewModel
class ChartViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val fetchStockCandlesUseCase: FetchStockCandlesUseCase,
    private val getStockCandlesFlowUseCase: GetStockCandlesFlowUseCase
) : AndroidViewModel(application) {

    private val symbol = savedStateHandle.get<String>("symbol") ?: ""

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _resolution = MutableLiveData<CandlesResolution>()

    private var _candles = MutableLiveData<DomainStockCandles>()
    val candles: LiveData<DomainStockCandles>
        get() = _candles

    fun bindData(resolution: CandlesResolution) = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING

            val curUTCTime: Long = DateTime.now().millis / 1000L
            fetchStockCandlesUseCase(
                symbol = symbol,
                resolution = resolution.value,
                fromTime = calculatePeriod(resolution),
                toTime = curUTCTime
            )

            _candles = getStockCandlesFlowUseCase(
                symbol,
                resolution.value
            ).asLiveData() as MutableLiveData<DomainStockCandles>

            _status.value = LoadingStatus.DONE
        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
        }
    }

    fun changeResolution(resolution: CandlesResolution) {
        _resolution.value = resolution
    }

    private fun calculatePeriod(resolution: CandlesResolution): Long {
        val curTime = DateTime.now()
        return when (resolution) {
            CandlesResolution.ONE_MIN -> {
                curTime.minusMinutes(1)
            }
            CandlesResolution.FIVE_MIN -> {
                curTime.minusMinutes(5)
            }
            CandlesResolution.FIFTEEN_MIN -> {
                curTime.minusMinutes(15)
            }
            CandlesResolution.THIRTY_MIN -> {
                curTime.minusMinutes(30)
            }
            CandlesResolution.SIXTY_MIN -> {
                curTime.minusHours(1)
            }
            CandlesResolution.ONE_DAY -> {
                curTime.minusDays(1)
            }
            CandlesResolution.ONE_WEEK -> {
                curTime.minusWeeks(1)
            }
            CandlesResolution.ONE_MONTH -> {
                curTime.minusMonths(1)
            }
        }.millis / 1000L
    }


}