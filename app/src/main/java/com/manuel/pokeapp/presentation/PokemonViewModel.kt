package com.manuel.pokeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuel.pokeapp.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PokemonUiState(
    val name: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class PokemonViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState

    fun fetchRandomPokemon() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val randomId = (1..RetrofitInstance.POKEMON_COUNT).random()
                val pokemon = RetrofitInstance.api.getPokemon(randomId)
                _uiState.value = PokemonUiState(
                    name = pokemon.name.replaceFirstChar { it.uppercase() },
                    imageUrl = pokemon.sprites.front_default ?: "",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar"
                )
            }
        }
    }
}

