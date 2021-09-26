package ru.mrfiring.stocktracker.presentation.details.news

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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
            viewModel.openSourceWebsite(it)
        }

        binding.newsNetwork.networkErrorImage.setOnClickListener { viewModel.retry() }
        binding.newsList.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner, ::applyState)
        viewModel.openWebpage.observe(viewLifecycleOwner) { openWebPage(it.articleUrl) }

        return binding.root
    }

    private fun applyState(state: NewsFragmentState) {
        when (state) {
            NewsFragmentState.Loading -> applyLoading()
            is NewsFragmentState.Content -> applyContent(state)
            NewsFragmentState.Error -> applyError()
        }
    }

    private fun applyLoading() {
        binding.newsProgress.visibility = View.VISIBLE
        binding.newsList.visibility = View.GONE
        binding.newsNetwork.networkErrorContainer.visibility = View.GONE
    }

    private fun applyContent(content: NewsFragmentState.Content) {
        if (content.news.isEmpty()) {
            binding.newsNoArticlesMessage.visibility = View.VISIBLE
        } else {
            val adapter = binding.newsList.adapter as NewsRecyclerViewAdapter
            adapter.submitList(content.news)
            binding.newsNoArticlesMessage.visibility = View.GONE
        }
        setIsLoaded()
    }

    private fun setIsLoaded() {
        binding.newsList.visibility = View.VISIBLE
        binding.newsProgress.visibility = View.GONE
    }

    private fun applyError() {
        binding.newsNetwork.networkErrorContainer.visibility = View.VISIBLE
        binding.newsProgress.visibility = View.GONE
        binding.newsList.visibility = View.GONE
        binding.newsNoArticlesMessage.visibility = View.GONE
    }

    private fun openWebPage(url: String) {
        val pageUri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, pageUri)
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {

        }
    }

    companion object {

        fun newInstance(symbol: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, symbol)
                }
            }
    }
}