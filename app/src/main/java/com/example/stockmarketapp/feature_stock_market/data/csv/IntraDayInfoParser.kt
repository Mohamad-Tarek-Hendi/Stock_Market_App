package com.example.stockmarketapp.feature_stock_market.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stockmarketapp.feature_stock_market.data.mapper.toIntraDayInfo
import com.example.stockmarketapp.feature_stock_market.data.remote.dto.IntraDayInfoDto
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayInfoParser @Inject constructor() : CSVParser<IntraDayInfo> {

    // Implementation of the parse function from the CSVParser interface
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        // Create a CSVReader to read CSV data from the input stream
        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            // Execute the following code block in the IO context (background thread)

            // Read all the rows from the CSV data
            csvReader
                .readAll()
                .drop(1)// Skip the header row

                .mapNotNull { line ->
                    // Extract timestamp from the line (index 0)
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null

                    // Extract closing price from the line (index 4)
                    val close = line.getOrNull(4) ?: return@mapNotNull null

                    // Create an IntraDayInfoDto object with timestamp and closing price
                    val dto = IntraDayInfoDto(timestamp, close.toDouble())

                    // Convert the dto to IntraDayInfo object
                    dto.toIntraDayInfo()
                }

                .filter {
                    // Keep only the IntraDayInfo objects with the date of the previous day
                    it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    // Sort the IntraDayInfo objects by hour
                    it.date.hour
                }

                // Close the CSV reader after processing all lines
                .also {
                    csvReader.close()
                }
        }
    }
}