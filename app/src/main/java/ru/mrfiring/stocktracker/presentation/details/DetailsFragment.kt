package ru.mrfiring.stocktracker.presentation.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.details_fragment,
            container, false)

        val detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        binding.detailsViewModel = detailsViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

}