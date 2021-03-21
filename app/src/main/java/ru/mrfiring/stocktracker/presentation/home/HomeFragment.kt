package ru.mrfiring.stocktracker.presentation.home

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
import ru.mrfiring.stocktracker.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val recyclerAdapter = HomeRecyclerViewAdapter(
            onClick = {
                homeViewModel.navigateToDetail(it.symbol)
            },
            onLongClick = {
                homeViewModel.markAsFavorite(it)
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

        homeViewModel.stocks.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                recyclerAdapter.submitData(it)
            }

        })

        homeViewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Network error occurred.", Toast.LENGTH_SHORT).show()
            }
        })

        binding.searchView.setOnClickListener {
            homeViewModel.navigateToSearch()
        }

        homeViewModel.navigateToSearchFragment.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        })

        homeViewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
        }

        return binding.root
    }

    fun setIsLoading() {
        binding.searchView.visibility = View.GONE
        binding.stockList.visibility = View.GONE
        binding.loadingBar.visibility = View.VISIBLE
    }

    fun setIsLoaded() {
        binding.searchView.visibility = View.VISIBLE
        binding.stockList.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
    }

}