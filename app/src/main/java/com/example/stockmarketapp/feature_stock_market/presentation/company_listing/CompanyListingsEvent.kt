package com.example.stockmarketapp.feature_stock_market.presentation.company_listing

sealed class CompanyListingsEvent {
    object Refresh : CompanyListingsEvent()
    data class OnSearchQueryChange(val query: String) : CompanyListingsEvent()
}
