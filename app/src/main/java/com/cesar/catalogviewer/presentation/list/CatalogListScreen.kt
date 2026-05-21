package com.cesar.catalogviewer.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cesar.catalogviewer.R
import com.cesar.catalogviewer.presentation.components.CatalogItemCard
import com.cesar.catalogviewer.presentation.components.CatalogSearchField
import com.cesar.catalogviewer.presentation.components.EmptyState
import com.cesar.catalogviewer.presentation.components.ErrorState

@Composable
fun CatalogListRoute(
    onItemClick: (String) -> Unit,
    viewModel: CatalogListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CatalogListEffect.NavigateToDetail -> onItemClick(effect.itemId)
            }
        }
    }

    CatalogListScreen(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogListScreen(
    state: CatalogListState,
    onIntent: (CatalogListIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.title_catalog_viewer))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CatalogSearchField(
                query = state.searchQuery,
                onQueryChange = { query ->
                    onIntent(CatalogListIntent.SearchQueryChanged(query))
                }
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.errorMessage != null -> {
                        ErrorState(
                            message = state.errorMessage,
                            onRetryClick = {
                                onIntent(CatalogListIntent.RetryClicked)
                            }
                        )
                    }

                    state.visibleItems.isEmpty() -> {
                        EmptyState(
                            message = if (state.searchQuery.isBlank()) {
                                "No catalog items available."
                            } else {
                                "No items match your search."
                            }
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.visibleItems,
                                key = { item -> item.id }
                            ) { item ->
                                CatalogItemCard(
                                    item = item,
                                    onClick = {
                                        onIntent(CatalogListIntent.ItemClicked(item.id))
                                    },
                                    onFavoriteClick = {
                                        onIntent(CatalogListIntent.FavoriteClicked(item.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
