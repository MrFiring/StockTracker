package ru.mrfiring.stocktracker.presentation.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.mrfiring.stocktracker.presentation.home.favorite.FavoriteFragment
import ru.mrfiring.stocktracker.presentation.home.stocks.StocksFragment

private const val NUM_PAGES = 2

class HomeFragmentPagerAdapter(fragment: HomeFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                StocksFragment()
            }
            1 -> {
                FavoriteFragment()
            }
            else -> {
                throw IndexOutOfBoundsException("Position: $position isn't supported")
            }
        }
    }
}