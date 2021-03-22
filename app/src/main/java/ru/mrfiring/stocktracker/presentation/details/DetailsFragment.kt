package ru.mrfiring.stocktracker.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.databinding.FragmentDetailsBinding

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val symbol: String = requireArguments().getString("symbol", "11111")

        val adapter = DetailsFragmentPagerAdapter(this, symbol)

        binding.detailPager.adapter = adapter

        TabLayoutMediator(binding.detailTabs, binding.detailPager) { tab, position ->
            tab.text = if (position == 0) {
                "General"
            } else {
                "News"
            }
        }.attach()

        return binding.root
    }

}