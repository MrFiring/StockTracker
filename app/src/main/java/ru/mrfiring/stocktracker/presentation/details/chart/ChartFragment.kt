package ru.mrfiring.stocktracker.presentation.details.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.databinding.FragmentChartBinding

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

        return binding.root
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