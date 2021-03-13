package ru.mrfiring.stocktracker.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.SearchFragmentBinding

@AndroidEntryPoint
class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: SearchFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.search_fragment, container, false)


        binding.lifecycleOwner = this

        return binding.root
    }

}