package ru.mrfiring.stocktracker.presentation.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: SearchFragmentBinding = DataBindingUtil.inflate(inflater,
        R.layout.search_fragment, container, false)

        val searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.searchViewModel = searchViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

}