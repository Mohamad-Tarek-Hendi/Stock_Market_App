package com.example.stockmarketapp.feature_stock_market.domain.model

import java.time.LocalDateTime

data class IntraDayInfo(
    // When we have both date and time like(2023-04-04 20:00:00) easily use localDateTime
    // If you have only Time use LocalTime
    // If you have only Date use LocalDate and so on..
    val date: LocalDateTime,
    val close: Double
)
