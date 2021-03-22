package ru.mrfiring.stocktracker.presentation.details.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.FragmentDetailsGeneralBinding
import ru.mrfiring.stocktracker.domain.DomainCompany

private const val ARG_PARAM = "symbol"

@AndroidEntryPoint
class DetailsGeneralFragment : Fragment() {

    private val generalViewModel: DetailsGeneralViewModel by viewModels()

    private lateinit var binding: FragmentDetailsGeneralBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsGeneralBinding.inflate(inflater, container, false)

        generalViewModel.company.observe(viewLifecycleOwner) {
            setupInformationCard(it)
        }

        generalViewModel.status.observe(viewLifecycleOwner) {
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

    companion object {
        @JvmStatic
        fun newInstance(symbol: String) =
            DetailsGeneralFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, symbol)
                }
            }
    }

}