package ru.mrfiring.stocktracker.presentation.details.chart

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.FragmentChartBinding
import ru.mrfiring.stocktracker.domain.DomainStockCandles
import ru.mrfiring.stocktracker.presentation.details.general.LoadingStatus

private const val ARG_PARAM = "symbol"

@AndroidEntryPoint
class ChartFragment : Fragment() {

    private lateinit var binding: FragmentChartBinding

    private val viewModel: ChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartBinding.inflate(inflater, container, false)


        //Setup chart base parameters
        with(binding.candleChart) {
            requestDisallowInterceptTouchEvent(true)
            legend.isEnabled = false
            description.isEnabled = false
            isAutoScaleMinMaxEnabled = true
            xAxis.setDrawGridLines(false)
            xAxis.setLabelCount(2, true)
            xAxis.setAvoidFirstLastClipping(true)
        }

        //Listen to button's toggle
        binding.chartResolutionGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.chartResolutionDay -> {
                    viewModel.bindData(CandlesResolution.DAY)
                }
                R.id.chartResolutionSixtyMin -> {
                    viewModel.bindData(CandlesResolution.SIXTY_MIN)
                }
                R.id.chartResolutionFiveMin -> {
                    viewModel.bindData(CandlesResolution.FIVE_MIN)
                }
                R.id.chartResolutionOneMin -> {
                    viewModel.bindData(CandlesResolution.ONE_MIN)
                }
            }
        }

        //Toggle button to load chart on startup.
        binding.chartResolutionDay.toggle()

        //Change x-labels format depend on selected resolution
        viewModel.resolution.observe(viewLifecycleOwner) {
            with(binding.candleChart) {
                candleData?.let { data ->
                    xAxis.valueFormatter = ChartLabelFormatter(
                        it,
                        data.getDataSetByIndex(0)
                    )
                }
            }
        }

        //Setup chart when data arrives
        viewModel.candles.observe(viewLifecycleOwner) {
            setupChart(it)
        }

        //Loading status observe
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    setIsLoading()
                }
                LoadingStatus.DONE -> {
                    setIsLoaded()
                }
                LoadingStatus.ERROR -> {
                    setIsError()
                }
            }
        }

        return binding.root
    }

    private fun setupChart(candles: DomainStockCandles) {
        val entries = mutableListOf<CandleEntry>()
        entries.addAll(
            candles.timestamp.mapIndexed { i, stamp ->
                CandleEntry(
                    i.toFloat(),
                    candles.high[i].toFloat(), candles.low[i].toFloat(),
                    candles.open[i].toFloat(), candles.close[i].toFloat(),
                    stamp
                )
            }
        )

        val candleDataSet = CandleDataSet(entries, "Candles")
        with(candleDataSet) {
            axisDependency = YAxis.AxisDependency.LEFT
            shadowColor = Color.DKGRAY
            shadowWidth = 1f
            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = Color.BLACK

        }

        val candleData = CandleData(candleDataSet)

        with(binding.candleChart) {
            data = candleData
            setVisibleXRangeMinimum(10f)
            setVisibleXRangeMaximum(30f)
            resetZoom()
            invalidate()
            moveViewToX((candleDataSet.entryCount).toFloat())
        }
    }

    private fun setIsLoading() {
        with(binding) {
            chartProgressBar.visibility = View.VISIBLE
            chartNetworkError.networkErrorContainer.visibility = View.GONE
            candleChart.visibility = View.GONE
        }
    }

    private fun setIsLoaded() {
        with(binding) {
            candleChart.visibility = View.VISIBLE
            chartProgressBar.visibility = View.GONE
        }
    }

    private fun setIsError() {
        with(binding) {
            chartNetworkError.networkErrorContainer.visibility = View.VISIBLE
            chartProgressBar.visibility = View.GONE
            candleChart.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(symbol: String) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, symbol)
                }
            }
    }
}