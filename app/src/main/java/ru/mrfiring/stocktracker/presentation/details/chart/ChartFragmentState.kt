package ru.mrfiring.stocktracker.presentation.details.chart

import ru.mrfiring.stocktracker.domain.DomainStockCandles

sealed class ChartFragmentState {

    object Loading : ChartFragmentState()

    data class Content(val candles: DomainStockCandles) : ChartFragmentState()

    object Error : ChartFragmentState()

}