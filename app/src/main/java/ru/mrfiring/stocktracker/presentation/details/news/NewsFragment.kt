package ru.mrfiring.stocktracker.presentation.details.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.databinding.FragmentNewsBinding

private const val ARG_PARAM = "symbol"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val viewModel: NewsFragmentViewModel by viewModels()

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        val adapter = NewsRecyclerViewAdapter {

        }

        binding.newsList.adapter = adapter

        viewModel.news.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitList(it)
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(symbol: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, symbol)
                }
            }
    }
}