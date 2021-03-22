package ru.mrfiring.stocktracker.presentation.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.databinding.FragmentFavoriteBinding

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
            onLongClick = { viewModel.updateSymbol(it) },
            onClick = { viewModel.navigateToDetail(it) }
        )

        binding.favoritesList.adapter = favoriteAdapter


        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                    it.symbol
                )
            )
        }


        return binding.root
    }

}