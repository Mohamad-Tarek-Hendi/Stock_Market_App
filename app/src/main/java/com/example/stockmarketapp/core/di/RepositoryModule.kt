package com.example.stockmarketapp.core.di

import com.example.stockmarketapp.feature_stock_market.data.csv.CSVParser
import com.example.stockmarketapp.feature_stock_market.data.csv.CompanyListingParser
import com.example.stockmarketapp.feature_stock_market.data.csv.IntraDayInfoParser
import com.example.stockmarketapp.feature_stock_market.data.repository.StockRepositoryImp
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo
import com.example.stockmarketapp.feature_stock_market.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>


    @Binds
    @Singleton
    abstract fun bindIntraDayParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfo>


    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImp
    ): StockRepository

}