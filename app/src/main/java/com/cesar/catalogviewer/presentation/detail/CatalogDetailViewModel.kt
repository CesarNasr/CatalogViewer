package com.cesar.catalogviewer.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.catalogviewer.domain.model.Resource
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import com.cesar.catalogviewer.domain.usecase.GetItemByIdUseCase
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
class CatalogDetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogDetailState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = CatalogDetailState()
    )

    private val _effect = Channel<CatalogDetailEffect>()
    val effect = _effect.receiveAsFlow()

    private var itemId: String? = null

    fun loadItem(itemId: String) {
        this.itemId = itemId

        viewModelScope.launch {
            getItemByIdUseCase(itemId).collect { resource ->
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
                        _state.update {
                            it.copy(
                                isLoading = false,
                                item = resource.data,
                                errorMessage = if (resource.data == null) {
                                    "Item not found."
                                } else {
                                    null
                                }
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

    fun onIntent(intent: CatalogDetailIntent) {
        when (intent) {
            CatalogDetailIntent.BackClicked -> navigateBack()
            is CatalogDetailIntent.FavoriteClicked -> toggleFavorite(intent.itemId)
            CatalogDetailIntent.RetryClicked -> itemId?.let(::loadItem)
        }
    }

    private fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(itemId)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(CatalogDetailEffect.NavigateBack)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
