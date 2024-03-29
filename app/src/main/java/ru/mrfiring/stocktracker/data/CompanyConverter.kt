package ru.mrfiring.stocktracker.data

import ru.mrfiring.stocktracker.data.database.DatabaseCompany
import ru.mrfiring.stocktracker.data.database.DatabaseCompanyNews
import ru.mrfiring.stocktracker.data.network.CompanyNews
import ru.mrfiring.stocktracker.data.network.CompanyProfile
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.DomainCompanyNews


fun CompanyProfile.asDatabaseObject(symbol: String): DatabaseCompany {
    return DatabaseCompany(
        symbol,
        exchange ?: "Unknown",
        ipo ?: "",
        marketCapitalization ?: 0.0,
        name ?: "Unknown",
        phone ?: "Unknown",
        shareOutStanding ?: 0.0,
        webUrl ?: "Unknown",
        logoUrl ?: "Unknown",
        finnhubIndustry ?: "Unknown"
    )
}

fun DatabaseCompany.asDomainObject(
): DomainCompany = DomainCompany(
    displaySymbol, exchange, marketCapitalization,
    name, phone, shareOutStanding, finhubIndustry
)

fun CompanyNews.asDatabaseObject(): DatabaseCompanyNews {
    return DatabaseCompanyNews(
        symbol, category, datetime, headline, id, imgUrl, sourceName, summary, articleUrl
    )
}

fun DatabaseCompanyNews.asDomainObject(): DomainCompanyNews {
    return DomainCompanyNews(
        symbol, category, datetime, headline, id, imgUrl, sourceName, summary, articleUrl
    )
}