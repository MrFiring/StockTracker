package ru.mrfiring.stocktracker.presentation.home.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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


        recyclerAdapter.addLoadStateListener {
            if (it.mediator?.refresh is LoadState.Loading) {
                setIsLoading()
            }
            if (it.source.refresh is LoadState.Loading) {
                setIsLoading()
            } else {
                setIsLoaded()
            }

        }

        binding.stockList.adapter = recyclerAdapter

        stocksViewModel.stocks.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                recyclerAdapter.submitData(it)
            }

        })

        stocksViewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Network error occurred.", Toast.LENGTH_SHORT).show()
            }
        })

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

    fun setIsLoading() {
        binding.stockList.visibility = View.GONE
        binding.loadingBar.visibility = View.VISIBLE
    }

    fun setIsLoaded() {
        binding.stockList.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
    }

}