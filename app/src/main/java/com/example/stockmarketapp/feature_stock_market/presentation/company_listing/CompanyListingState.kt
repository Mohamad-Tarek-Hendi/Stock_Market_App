package com.example.stockmarketapp.feature_stock_market.presentation.company_listing

import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing

data class CompanyListingState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = ""
)
