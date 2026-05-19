package com.cesar.catalogviewer.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.catalogviewer.domain.model.Resource
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import com.cesar.catalogviewer.domain.usecase.GetCatalogItemsUseCase
import com.cesar.catalogviewer.domain.usecase.SearchCatalogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CatalogListViewModel @Inject constructor(
    private val getCatalogItemsUseCase: GetCatalogItemsUseCase,
    private val searchCatalogUseCase: SearchCatalogUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogListState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = CatalogListState()
    )

    private val _effect = Channel<CatalogListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadCatalog()
    }

    fun onIntent(intent: CatalogListIntent) {
        when (intent) {
            is CatalogListIntent.SearchQueryChanged -> onSearchQueryChanged(intent.query)
            is CatalogListIntent.ItemClicked -> navigateToDetail(intent.itemId)
            is CatalogListIntent.FavoriteClicked -> toggleFavorite(intent.itemId)
            CatalogListIntent.RetryClicked -> loadCatalog()
        }
    }

    private fun loadCatalog() {
        viewModelScope.launch {
            getCatalogItemsUseCase().collect { resource ->
                when (resource) {
                    Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update { currentState ->
                            val visibleItems = searchCatalogUseCase(
                                query = currentState.searchQuery,
                                items = resource.data
                            )

                            currentState.copy(
                                isLoading = false,
                                items = resource.data,
                                visibleItems = visibleItems,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _state.update { currentState ->
            currentState.copy(
                searchQuery = query,
                visibleItems = searchCatalogUseCase(
                    query = query,
                    items = currentState.items
                )
            )
        }
    }

    private fun navigateToDetail(itemId: String) {
        viewModelScope.launch {
            _effect.send(CatalogListEffect.NavigateToDetail(itemId))
        }
    }

    private fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(itemId)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
