package com.example.stockmarketapp.feature_stock_market.data.csv

import java.io.InputStream
import java.util.stream.BaseStream

interface CSVParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}