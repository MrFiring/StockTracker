package ru.mrfiring.stocktracker.presentation.details.news

import ru.mrfiring.stocktracker.domain.DomainCompanyNews

sealed class NewsFragmentState {

    object Loading : NewsFragmentState()

    data class Content(val news: List<DomainCompanyNews>) : NewsFragmentState()

    object Error : NewsFragmentState()
}
