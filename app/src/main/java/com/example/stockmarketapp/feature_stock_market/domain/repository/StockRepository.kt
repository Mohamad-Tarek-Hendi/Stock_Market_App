package com.example.stockmarketapp.feature_stock_market.domain.repository

import com.example.stockmarketapp.core.util.Resource
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyInfo
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo
import kotlinx.coroutines.flow.Flow

interface StockRepository {


    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getInterDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}