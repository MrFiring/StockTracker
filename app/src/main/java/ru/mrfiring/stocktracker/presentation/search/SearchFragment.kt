package ru.mrfiring.stocktracker.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mrfiring.stocktracker.databinding.SearchFragmentBinding

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SearchFragmentBinding =
            SearchFragmentBinding.inflate(inflater, container, false)

        val searchHistoryViewAdapter = SearchHistoryViewAdapter {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.searchHistoryList.adapter = searchHistoryViewAdapter

        viewModel.history.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                searchHistoryViewAdapter.submitList(it)
            }
        }

        return binding.root
    }

}