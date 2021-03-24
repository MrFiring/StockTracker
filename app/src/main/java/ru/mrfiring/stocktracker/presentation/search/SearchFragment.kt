package ru.mrfiring.stocktracker.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.databinding.SearchFragmentBinding
import ru.mrfiring.stocktracker.domain.DomainQuote
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

enum class LoadingStatus {
    LOADING, ERROR, DONE
}

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)

        //Setup adapters
        val searchHistoryViewAdapter = SearchHistoryViewAdapter {
            binding.searchQuery.setText(it)
        }

        val searchResultsRecyclerViewAdapter = SearchResultsRecyclerViewAdapter {
            viewModel.onNavigateToDetail(it)
        }

        binding.searchHistoryList.adapter = searchHistoryViewAdapter
        binding.searchResultsList.adapter = searchResultsRecyclerViewAdapter
        //end setup adapters

        //Bind data into lists.
        viewModel.history.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                searchHistoryViewAdapter.submitList(it)
            }
        }
        viewModel.searchResults.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                searchResultsRecyclerViewAdapter.submitList(
                    it.map {
                        DomainStockSymbol(
                            it.displaySymbol,
                            it.description,
                            "",
                            DomainQuote(0.0, 0.0, 0.0, 0.0, 0.0),
                            false
                        )
                    }
                )
            }
        }

        //Show loading
        viewModel.status.observe(viewLifecycleOwner) {

            it.searchStatus?.let { state ->
                when (state) {
                    LoadingStatus.LOADING -> {
                        setResultsIsLoading()
                    }
                    LoadingStatus.DONE -> {
                        setResultsIsLoaded()
                    }
                    else -> {
                        showNetworkError()
                    }
                }

            }
            when (it.historyStatus) {
                LoadingStatus.LOADING -> {
                    setHistoryIsLoading()
                }
                LoadingStatus.DONE -> {
                    setHistoryIsLoaded()
                }
                else -> {
                }

            }
        }
        //Listening to text changing of editText
        binding.searchQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                viewModel.emitNewQuery(text)
            }
        })

        //Show history by pressing "clear_text" button of editText
        binding.textInputLayout.setEndIconOnClickListener {
            viewModel.rebindHistory()
        }

        //Navigation
        viewModel.navigateToDetail.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it.symbol)
            )
        }

        return binding.root
    }

    private fun showNetworkError() {
        binding.searchNetworkError.networkErrorContainer.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryContainer.visibility = View.GONE
        binding.searchResultsContainer.visibility = View.GONE
    }

    private fun setHistoryIsLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.searchNetworkError.networkErrorContainer.visibility = View.GONE
        binding.searchHistoryContainer.visibility = View.GONE
        binding.searchResultsContainer.visibility = View.GONE
    }

    private fun setHistoryIsLoaded() {
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryContainer.visibility = View.VISIBLE
        binding.searchResultsContainer.visibility = View.GONE
    }

    private fun setResultsIsLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.searchHistoryContainer.visibility = View.GONE
        binding.searchResultsContainer.visibility = View.GONE
    }

    private fun setResultsIsLoaded() {
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryContainer.visibility = View.GONE
        binding.searchResultsContainer.visibility = View.VISIBLE
    }

}