package com.example.stockmarketapp.feature_stock_market.data.repository

import com.example.stockmarketapp.core.util.Resource
import com.example.stockmarketapp.feature_stock_market.data.csv.CSVParser
import com.example.stockmarketapp.feature_stock_market.data.csv.CompanyListingParser
import com.example.stockmarketapp.feature_stock_market.data.csv.IntraDayInfoParser
import com.example.stockmarketapp.feature_stock_market.data.local.StockDatabase
import com.example.stockmarketapp.feature_stock_market.data.mapper.toCompanyInfo
import com.example.stockmarketapp.feature_stock_market.data.mapper.toCompanyListing
import com.example.stockmarketapp.feature_stock_market.data.mapper.toCompanyListingEntity
import com.example.stockmarketapp.feature_stock_market.data.remote.StockApi
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyInfo
import com.example.stockmarketapp.feature_stock_market.domain.model.CompanyListing
import com.example.stockmarketapp.feature_stock_market.domain.model.IntraDayInfo
import com.example.stockmarketapp.feature_stock_market.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImp @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {


    private val dao = db.dao
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {

            /**
            Start
             * Emit loading state
             * Get data from database
             * Emit data from database
             * Check if database is empty and query is blank
             * Check if we should just load from cache
             * If yes, emit loading state and return
             * Try to get data from remote API
             * If fails, emit error state
             * If success, save data to database and emit success state
             * Emit loading state
            End
             */

            // Emit a loading state to notify the UI
            emit(Resource.Loading(true))

            // Retrieve the company listings from the local database
            val localListing = dao.searchCompanyListing(query)

            // Emit a success state with the mapped local listings
            emit(Resource.Success(
                data = localListing.map {
                    it.toCompanyListing()
                }
            ))

            // Check if the local database is empty and the query is blank
            val isDbEmpty = localListing.isEmpty() && query.isBlank()


            // Determine if we should just load from the cache without fetching from the remote
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                // Emit a loading state to indicate that loading is done
                emit(Resource.Loading(false))
                //exit the current lambda function and terminate the flow when the shouldJustLoadFromCache condition is met.
                return@flow
            }

            // Fetch the company listings from the remote API
            val remoteListing = try {
                val response = api.getCompanyListing()
                // To read CSV file
                companyListingParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't Load Data.."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't Load Data.."))
                null
            }

            remoteListing?.let { listings ->
                // If remoteListing is not null, execute the following code block

                // Clear existing company listings in the DAO
                dao.clearCompanyListing()

                // Upsert (insert or update) the new company listings in the DAO
                dao.upsertCompanyListing(listings.map {
                    it.toCompanyListingEntity()
                })

                // Emit a success state with the updated company listings from the DAO
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("").map {
                            it.toCompanyListing()
                        }
                    )
                )

                emit(Resource.Loading(false))
            }

        }
    }

    override suspend fun getInterDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {

            val response = api.getIntraDayInfo(symbol = symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            Resource.Success(result)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load interDay info.."
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load interDay info.."
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info.."
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info.."
            )
        }
    }
}