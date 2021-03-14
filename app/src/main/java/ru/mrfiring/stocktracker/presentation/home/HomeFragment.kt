package ru.mrfiring.stocktracker.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
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
        val binding: HomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment,
            container, false
        )


        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        binding.stockList.adapter =
            HomeRecyclerViewAdapter(HomeRecyclerViewAdapter.ClickListener() {
                Toast.makeText(context, it.companyName, Toast.LENGTH_SHORT).show()
            })

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