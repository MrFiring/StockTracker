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
import ru.mrfiring.stocktracker.presentation.details.general.LoadingStatus

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

        binding.newsNetwork.networkErrorImage.setOnClickListener {
            viewModel.retry()
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    setIsLoading()
                }
                LoadingStatus.DONE -> {
                    setIsLoaded()
                }
                else -> {
                    setIsError()
                }
            }
        }

        return binding.root
    }

    private fun setIsLoading() {
        binding.newsProgress.visibility = View.VISIBLE
        binding.newsList.visibility = View.GONE
        binding.newsNetwork.networkErrorContainer.visibility = View.GONE
    }

    private fun setIsLoaded() {
        binding.newsList.visibility = View.VISIBLE
        binding.newsProgress.visibility = View.GONE
    }

    private fun setIsError() {
        binding.newsNetwork.networkErrorContainer.visibility = View.VISIBLE
        binding.newsProgress.visibility = View.GONE
        binding.newsList.visibility = View.GONE
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