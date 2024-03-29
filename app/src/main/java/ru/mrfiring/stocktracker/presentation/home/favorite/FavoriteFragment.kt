package ru.mrfiring.stocktracker.presentation.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.databinding.FragmentFavoriteBinding
import ru.mrfiring.stocktracker.presentation.home.HomeFragmentDirections

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)

        val favoriteAdapter = FavoriteRecyclerViewAdapter(
            onLongClick = viewModel::updateSymbol,
            onClick = viewModel::navigateToDetail
        )

        binding.favoritesList.adapter = favoriteAdapter

        viewModel.favorites.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                favoriteAdapter.submitList(it)
                binding.noFavoritesText.isVisible = it.isEmpty()
            }
        }


        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    it.symbol
                )
            )
        }


        return binding.root
    }

}