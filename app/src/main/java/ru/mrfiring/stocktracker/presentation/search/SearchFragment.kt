package ru.mrfiring.stocktracker.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.databinding.SearchFragmentBinding

@AndroidEntryPoint
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SearchFragmentBinding =
            SearchFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

}