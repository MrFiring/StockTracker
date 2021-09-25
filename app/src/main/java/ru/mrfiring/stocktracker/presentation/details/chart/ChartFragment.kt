package ru.mrfiring.stocktracker.presentation.details.chart

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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

        initialChartSetup()

        //Listen to button's toggle
        initResolutionListener()

        //Toggle button to load chart on startup.
        binding.chartResolutionDay.toggle()

        //Change x-labels format depend on selected resolution
        viewModel.resolution.observe(viewLifecycleOwner, ::applyResolution)
        //Setup chart when data arrives
        viewModel.state.observe(viewLifecycleOwner, ::applyState)

        return binding.root
    }

    private fun initialChartSetup() {
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
    }

    private fun initResolutionListener() {
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
    }

    private fun applyResolution(resolution: CandlesResolution) {
        with(binding.candleChart) {
            candleData?.let { data ->
                xAxis.valueFormatter = ChartLabelFormatter(
                    resolution,
                    data.getDataSetByIndex(0)
                )
            }
        }
    }

    private fun applyState(state: ChartFragmentState) {
        when (state) {
            ChartFragmentState.Loading -> {
                with(binding) {
                    chartProgressBar.isVisible = true
                    chartNetworkError.networkErrorContainer.isVisible = false
                    candleChart.isVisible = false
                }
            }

            is ChartFragmentState.Content -> {
                setupChart(state.candles)
                with(binding) {
                    candleChart.isVisible = true
                    chartProgressBar.isVisible = false
                }
            }

            ChartFragmentState.Error -> {
                with(binding) {
                    chartNetworkError.networkErrorContainer.isVisible = true
                    chartProgressBar.isVisible = false
                    candleChart.isVisible = false
                }
            }
        }
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