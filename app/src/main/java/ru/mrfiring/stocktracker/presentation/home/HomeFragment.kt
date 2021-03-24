package ru.mrfiring.stocktracker.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val pagerAdapter = HomeFragmentPagerAdapter(this)

        binding.homePager.adapter = pagerAdapter
        TabLayoutMediator(binding.homeTabs, binding.homePager) { tab, position ->
            tab.text = if (position == 0) {
                getString(R.string.stocks)
            } else {
                getString(R.string.favorite)
            }
        }.attach()

        binding.homeSearchEditText.setOnClickListener {
            viewModel.navigateToSearch()
        }

        viewModel.navigateToSearchFragment.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            )
        }

        return binding.root
    }

}