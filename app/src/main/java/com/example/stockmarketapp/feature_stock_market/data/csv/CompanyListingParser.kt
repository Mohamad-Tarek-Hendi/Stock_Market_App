package com.example.stockmarketapp.feature_stock_market.data.csv

import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor() : CSVParser<CompanyListing> {

    // Implementation of the parse function from the CSVParser interface
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        // Create a CSVReader to read CSV data from the input stream
        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            // Execute the following code block in the IO context (background thread)

            // Read all the rows from the CSV data
            csvReader
                .readAll()
                .drop(1)// Skip the header row

                // Map each line to a CompanyListing object, filtering out any lines with missing values
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)


                    // Create a CompanyListing object if all values are non-null, otherwise skip the line
                    CompanyListing(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }

                // Close the CSV reader after processing all lines
                .also {
                    csvReader.close()
                }
        }
    }
}