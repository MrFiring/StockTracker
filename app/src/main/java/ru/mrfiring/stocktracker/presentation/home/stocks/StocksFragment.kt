package ru.mrfiring.stocktracker.presentation.home.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.StocksFragmentBinding
import ru.mrfiring.stocktracker.presentation.home.HomeFragmentDirections

@AndroidEntryPoint
class StocksFragment : Fragment() {

    private val stocksViewModel: StocksViewModel by viewModels()

    lateinit var binding: StocksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StocksFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val recyclerAdapter = StocksRecyclerViewAdapter(
            onClick = {
                stocksViewModel.navigateToDetail(it.symbol)
            },
            onLongClick = {
                stocksViewModel.markAsFavorite(it)
            }
        )

        //Handle paging loadStates
        recyclerAdapter.addLoadStateListener { state ->
            state.mediator?.let {
                if (it.refresh is LoadState.Loading) {
                    setIsLoading()
                } else if (it.refresh is LoadState.Error) {
                    stocksViewModel.onLoadingNetworkError()
                }
                //Used to start loading of stocks on first run.
                if (it.prepend is LoadState.Loading) {
                    stocksViewModel.onColdStart()
                }
            }
            if (state.refresh is LoadState.Loading) {
                setIsLoading()
            } else {
                setIsLoaded()
            }

        }

        stocksViewModel.coldStartIndicator.observe(viewLifecycleOwner) {
            stocksViewModel.coldQuotesUpdate(it)
        }

        binding.stockList.adapter = recyclerAdapter

        //bind data
        stocksViewModel.stocks.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                recyclerAdapter.submitData(it)
            }
        }

        //Handle error if initial loading fails
        stocksViewModel.isNetworkLoadingError.observe(viewLifecycleOwner) {
            setIsInitialLoadingError()
            Snackbar
                .make(
                    requireParentFragment().requireView(),
                    getString(R.string.loading_stocks_failed),
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction(getString(R.string.retry)) {
                    recyclerAdapter.retry()
                }
                .show()
        }

        //Handle error if refreshing of stock quotes fails
        stocksViewModel.isRefreshError.observe(viewLifecycleOwner) {
            Snackbar
                .make(
                    requireParentFragment().requireView(),
                    getString(R.string.refreshing_quotes_failed),
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction(getString(R.string.retry)) {
                    stocksViewModel.retryRefreshQuotes()
                }
                .show()
        }

        stocksViewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            this.findNavController()
                .navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        it
                    )
                )
        }

        return binding.root
    }

    private fun setIsLoading() {
        binding.loadingBar.visibility = View.VISIBLE
        binding.stockList.visibility = View.GONE
        binding.stocksNetworkError.networkErrorContainer.visibility = View.GONE
    }

    private fun setIsLoaded() {
        binding.stockList.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
    }

    private fun setIsInitialLoadingError() {
        binding.stocksNetworkError.networkErrorContainer.visibility = View.VISIBLE
        binding.stocksNetworkError.networkErrorText.visibility = View.GONE
        binding.stockList.visibility = View.GONE
        binding.loadingBar.visibility = View.GONE
    }
}