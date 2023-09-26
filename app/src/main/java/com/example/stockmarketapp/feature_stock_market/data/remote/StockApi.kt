package com.example.stockmarketapp.feature_stock_market.data.remote

import com.example.stockmarketapp.feature_stock_market.data.remote.dto.CompanyInfoDto
import com.example.stockmarketapp.feature_stock_market.data.util.Constant
import com.example.stockmarketapp.feature_stock_market.data.util.Constant.API_KEY
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getCompanyListing(
        @Query("apikey") apikey: String = API_KEY
    ): ResponseBody


    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntraDayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto

}