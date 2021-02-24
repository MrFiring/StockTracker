package ru.mrfiring.stocktracker.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment,
        container, false)

        val application = requireNotNull(activity).application
        val viewModelFactory = HomeViewModelFactory(application)
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        binding.stockList.adapter = HomeRecyclerViewAdapter(HomeRecyclerViewAdapter.ClickListener(){
            Toast.makeText(context, it.companyName, Toast.LENGTH_SHORT).show()
        })

        homeViewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context, "Network error occured.", Toast.LENGTH_SHORT).show()
                homeViewModel.networkErrorShown()
            }
        })

        homeViewModel.navigateToSearchFragment.observe(viewLifecycleOwner, Observer {
            if(it){
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                homeViewModel.navigateToSearchCompleted()
            }
        })

        return binding.root
    }


}