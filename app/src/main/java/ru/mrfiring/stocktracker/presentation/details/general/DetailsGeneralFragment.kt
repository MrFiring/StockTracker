package ru.mrfiring.stocktracker.presentation.details.general

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.FragmentDetailsGeneralBinding
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.DomainQuote

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

        generalViewModel.stock.observe(viewLifecycleOwner) {
            setupPriceCard(it.quote)
            setupFavoriteMark(it.isFavorite)
        }

        binding.favoriteBtn.setOnClickListener {
            generalViewModel.markAsFavorite()
        }

        binding.generalNetwork.networkErrorImage.setOnClickListener {
            generalViewModel.retry()
        }

        generalViewModel.unsupportedSymbol.observe(viewLifecycleOwner) {
            binding.detailStockCard.visibility = View.GONE
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
                    setIsError()
                }
            }
        }

        return binding.root
    }

    private fun setupInformationCard(company: DomainCompany) {
        binding.apply {
            detailCompanyName.text = company.name

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

    @SuppressLint("StringFormatInvalid")
    private fun setupPriceCard(quote: DomainQuote) {
        binding.apply {
            detailDayLow.text = getString(
                R.string.format_current_price,
                quote.dayLow
            )
            detailDayHigh.text = getString(
                R.string.format_current_price,
                quote.dayHigh
            )
            detailCurrentPrice.text = getString(
                R.string.format_current_price,
                quote.current
            )

            val deltaPrice = quote.getDeltaPrice()
            if (deltaPrice >= 0) {
                detailDeltaPrice.setTextColor(Color.GREEN)
            } else {
                detailDeltaPrice.setTextColor(Color.RED)
            }

            detailDeltaPrice.text = getString(
                R.string.format_delta_price,
                deltaPrice,
                quote.getDeltaPricePercent()
            )

            detailDayOpen.text = getString(
                R.string.format_day_open,
                quote.dayOpen
            )
            detailPreviousOpen.text = getString(
                R.string.format_previous_day_open,
                quote.previousDayOpen
            )
        }
    }

    private fun setupFavoriteMark(isFavorite: Boolean) {
        binding.apply {
            if (isFavorite) {

                favoriteBtn.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        android.R.drawable.btn_star_big_on,
                        null
                    )
                )
            } else {
                favoriteBtn.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        android.R.drawable.btn_star_big_off,
                        null
                    )
                )
            }
        }
    }

    private fun setIsLoading() {
        binding.detailLoadingBar.visibility = View.VISIBLE
        binding.companyCard.visibility = View.GONE
        binding.detailStockCard.visibility = View.GONE
        binding.generalNetwork.networkErrorContainer.visibility = View.GONE
    }

    private fun setIsLoaded() {
        binding.companyCard.visibility = View.VISIBLE
        if (generalViewModel.unsupportedSymbol.value == null) {
            binding.detailStockCard.visibility = View.VISIBLE
        }
        binding.detailLoadingBar.visibility = View.GONE
    }

    private fun setIsError() {
        binding.generalNetwork.networkErrorContainer.visibility = View.VISIBLE
        binding.detailLoadingBar.visibility = View.GONE
        binding.companyCard.visibility = View.GONE
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