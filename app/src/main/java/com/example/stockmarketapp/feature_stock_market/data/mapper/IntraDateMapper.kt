package com.example.stockmarketapp.feature_stock_market.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stockmarketapp.feature_stock_market.data.remote.dto.IntraDayInfoDto
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {
    // Define the pn for the expected timestamp format (from source)
    val pattern = "yyyy-MM-dd HH:mm:ss"

    // Create a DateTimeFormatter using the pattern and default locale
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

    // Parse the timestamp string into a LocalDateTime object using the formatter
    val localDateTime = LocalDateTime.parse(timestamp, formatter)

    // Create a new IntraDayInfo object with the parsed date and close value
    return IntraDayInfo(
        date = localDateTime,
        close = close
    )
}