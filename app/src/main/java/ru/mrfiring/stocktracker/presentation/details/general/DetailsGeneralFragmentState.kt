package ru.mrfiring.stocktracker.presentation.details.general

import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

sealed class DetailsGeneralFragmentState {
    object Loading : DetailsGeneralFragmentState()

    data class Content(
        val company: DomainCompany?,
        val stockAndQuote: DomainStockSymbol?
    ) : DetailsGeneralFragmentState()

    object Error : DetailsGeneralFragmentState()
}
