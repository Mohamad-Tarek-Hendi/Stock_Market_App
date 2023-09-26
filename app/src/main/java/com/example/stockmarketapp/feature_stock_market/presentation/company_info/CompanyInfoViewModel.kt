package com.example.stockmarketapp.feature_stock_market.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.core.util.Resource
import com.example.stockmarketapp.feature_stock_market.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            // Retrieve the symbol from the savedStateHandle
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch

            // Update the state to indicate loading
            state = state.copy(isLoading = true)

            // Asynchronously fetch interDayInfo from the repository
            val interDayInfoResult = async {
                repository.getInterDayInfo(symbol)
            }

            // Asynchronously fetch companyInfo from the repository
            val companyInfoResult = async {
                repository.getCompanyInfo(symbol)
            }

            // Handle the result of fetching companyInfo
            //await is called, it suspends the coroutine until the result of the async operation is available
            when (val result = companyInfoResult.await()) {

                is Resource.Success -> {
                    // Update the state with successful companyInfo
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    // Update the state with error in fetching companyInfo
                    state = state.copy(
                        company = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                else -> Unit

            }

            // Handle the result of fetching interDayInfo
            when (val result = interDayInfoResult.await()) {

                // Update the state with successful interDayInfo
                is Resource.Success -> {
                    state = state.copy(
                        stockInfo = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    // Update the state with error in fetching interDayInfo
                    state = state.copy(
                        company = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                else -> Unit

            }
        }
    }
}