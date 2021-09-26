package ru.mrfiring.stocktracker.presentation.details.chart

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.mrfiring.stocktracker.SingleLiveEvent
import ru.mrfiring.stocktracker.domain.FetchStockCandlesUseCase
import ru.mrfiring.stocktracker.domain.GetStockCandlesUseCase
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
    savedStateHandle: SavedStateHandle,
    private val fetchStockCandlesUseCase: FetchStockCandlesUseCase,
    private val getStockCandlesUseCase: GetStockCandlesUseCase
) : ViewModel() {

    private val symbol = savedStateHandle.get<String>("symbol") ?: ""

    private val _state = MutableLiveData<ChartFragmentState>()
    val state: LiveData<ChartFragmentState> = _state

    private var _resolution = SingleLiveEvent<CandlesResolution>()
    val resolution: LiveData<CandlesResolution> = _resolution

    @SuppressLint("NullSafeMutableLiveData")
    fun bindData(resolution: CandlesResolution) = viewModelScope.launch {
        try {
            _state.value = ChartFragmentState.Loading

            val curDt = DateTime.now()
            fetchStockCandlesUseCase(
                symbol = symbol,
                resolution = resolution.value,
                fromTime = curDt.asUtcMinusYears(1),
                toTime = curDt.asUtc()
            )

            val candles = getStockCandlesUseCase(symbol, resolution.value)

            _state.value = ChartFragmentState.Content(candles)
            _resolution.value = resolution

        } catch (ex: IOException) {
            _state.value = ChartFragmentState.Error
            Log.e("Chart", ex.stackTraceToString())
        }
    }

    private fun DateTime.asUtc(): Long = millis / 1000L

    private fun DateTime.asUtcMinusYears(years: Int): Long = minusYears(years).millis / 1000L

}