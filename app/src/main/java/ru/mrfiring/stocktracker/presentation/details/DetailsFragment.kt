package ru.mrfiring.stocktracker.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.DetailsFragmentBinding
import ru.mrfiring.stocktracker.domain.DomainCompany
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModel.AssistedFactory

    private lateinit var binding: DetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val argSymbol = DetailsFragmentArgs.fromBundle(requireArguments()).symbol
        val viewModel: DetailsViewModel = ViewModelProvider(
            this,
            DetailsViewModel.provideFactory(
                detailsViewModelFactory,
                argSymbol
            )
        ).get()

        viewModel.company.observe(viewLifecycleOwner) {
            setupInformationCard(it)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    setIsLoading()
                }
                LoadingStatus.DONE -> {
                    setIsLoaded()
                }
                else -> {

                }
            }
        }

        return binding.root
    }

    private fun setupInformationCard(company: DomainCompany) {
        binding.apply {
            detailCompanyName.text = company.name
            detailTicker.text = company.symbol

            detailPhone.text = getString(
                R.string.format_phone,
                company.phone
            )
            detailExchange.text = getString(
                R.string.format_exchange,
                company.exchange
            )
            detailIndustry.text = getString(
                R.string.format_industry,
                company.finhubIndustry
            )


            detailMarketCap.text = getString(
                R.string.format_market_cap,
                company.marketCapitalization
            )
            detailOutstanding.text = getString(
                R.string.format_number_outstanding,
                company.shareOutStanding
            )


        }


    }

    private fun setIsLoading() {
        binding.detailLoadingBar.visibility = View.VISIBLE
        binding.companyCard.visibility = View.GONE
    }

    private fun setIsLoaded() {
        binding.detailLoadingBar.visibility = View.GONE
        binding.companyCard.visibility = View.VISIBLE
    }

}