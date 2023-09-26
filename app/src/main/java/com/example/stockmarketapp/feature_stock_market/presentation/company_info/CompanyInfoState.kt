package com.example.stockmarketapp.feature_stock_market.presentation.company_info

import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyInfo
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
