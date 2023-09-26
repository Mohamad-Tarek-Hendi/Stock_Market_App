package com.example.stockmarketapp.feature_stock_market.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CompanyListing(
    val name: String,
    val symbol: String,
    val exchange: String,
)
