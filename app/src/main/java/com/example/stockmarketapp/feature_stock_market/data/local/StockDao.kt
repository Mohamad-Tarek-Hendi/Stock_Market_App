package com.example.stockmarketapp.feature_stock_market.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCompanyListing(companyListingEntity: List<CompanyListingEntity>)

    @Query("DELETE FROM CompanyListingEntity")
    suspend fun clearCompanyListing()

    @Query(
        """
            SELECT * 
            FROM CompanyListingEntity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' 
            OR
            UPPER(:query) == symbol 
        """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>

}