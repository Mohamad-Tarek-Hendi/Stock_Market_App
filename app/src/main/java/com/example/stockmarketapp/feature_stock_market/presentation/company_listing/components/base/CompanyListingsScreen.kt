package com.example.stockmarketapp.feature_stock_market.presentation.company_listing.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarketapp.feature_stock_market.presentation.company_listing.CompanyListingsEvent
import com.example.stockmarketapp.feature_stock_market.presentation.company_listing.CompanyListingsViewModel
import com.example.stockmarketapp.feature_stock_market.presentation.company_listing.components.CompanyItem
import com.example.stockmarketapp.feature_stock_market.presentation.destinations.CompanyInfoScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RootNavGraph(start = true)
@Destination()
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    Column(modifier = Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingsEvent.OnSearchQueryChange(it)
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingsEvent.Refresh)
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companies.size) { i ->
                    val company = state.companies[i]

                    CompanyItem(company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(company.symbol)
                                )
                            }
                            .padding(16.dp)
                    )

                    if (i < state.companies.size) {
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            )
                        )
                    }
                }
            }
        }
    }


}