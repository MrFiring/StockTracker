package ru.mrfiring.stocktracker.presentation.details.chart

import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import org.joda.time.Instant

private const val FORMAT_DAY_RESOLUTION = "dd.MM.YY"
private const val FORMAT_MIN_RESOLUTION = "HH:mm $FORMAT_DAY_RESOLUTION"

class ChartLabelFormatter(
    private val resolution: CandlesResolution,
    private val candleDataSet: ICandleDataSet
) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        var entryIndex = value.toInt()

        if (entryIndex >= candleDataSet.entryCount) {
            entryIndex = candleDataSet.entryCount - 1
        }
        
        val entryData = candleDataSet.getEntryForIndex(entryIndex).data
        if (entryData is Long) {
            return if (resolution == CandlesResolution.DAY) {
                Instant.ofEpochSecond(entryData).toDateTime().toString(FORMAT_DAY_RESOLUTION)
            } else {
                Instant.ofEpochSecond(entryData).toDateTime().toString(
                    FORMAT_MIN_RESOLUTION
                )
            }
        }
        return super.getFormattedValue(value)
    }
}

