package ru.mrfiring.stocktracker.presentation.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.mrfiring.stocktracker.presentation.details.chart.ChartFragment
import ru.mrfiring.stocktracker.presentation.details.general.DetailsGeneralFragment
import ru.mrfiring.stocktracker.presentation.details.news.NewsFragment

private const val NUM_PAGES = 3

class DetailsFragmentPagerAdapter(
    fragment: Fragment,
    private val symbol: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DetailsGeneralFragment.newInstance(symbol)
            }
            1 -> {
                ChartFragment.newInstance(symbol)
            }
            2 -> {
                NewsFragment.newInstance(symbol)
            }
            else -> {
                throw IndexOutOfBoundsException("Position $position not supported")
            }

        }
    }
}