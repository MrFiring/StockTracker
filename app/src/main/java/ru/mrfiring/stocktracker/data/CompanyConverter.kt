package ru.mrfiring.stocktracker.data

import ru.mrfiring.stocktracker.data.database.DatabaseCompany
import ru.mrfiring.stocktracker.data.network.CompanyProfile
import ru.mrfiring.stocktracker.domain.DomainCompany


fun CompanyProfile.asDatabaseObject(symbol: String): DatabaseCompany {
    return DatabaseCompany(
        symbol,
        exchange,
        ipo,
        marketCapitalization,
        name,
        phone,
        shareOutStanding,
        webUrl,
        logoUrl,
        finnhubIndustry
    )
}

fun DatabaseCompany.asDomainObject(
): DomainCompany = DomainCompany(
    displaySymbol, exchange, marketCapitalization,
    name, phone, shareOutStanding, finhubIndustry
)