package ru.mrfiring.stocktracker.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.databinding.HomeFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeFragmentBinding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val recyclerAdapter = HomeRecyclerViewAdapter(HomeRecyclerViewAdapter.ClickListener {
            Toast.makeText(context, it.companyName, Toast.LENGTH_SHORT).show()
        })

        binding.stockList.adapter = recyclerAdapter

        homeViewModel.viewModelScope.launch {
            homeViewModel.getStocksFlow().collect {
                recyclerAdapter.submitList(it)
            }
        }

        homeViewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Network error occurred.", Toast.LENGTH_SHORT).show()
            }
        })

        homeViewModel.navigateToSearchFragment.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        })

        return binding.root
    }


}