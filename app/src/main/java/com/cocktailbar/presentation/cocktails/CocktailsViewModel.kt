package com.cocktailbar.presentation.cocktails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocktailbar.domain.use_case.GetCocktailsUseCase
import com.cocktailbar.presentation.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailsViewModel @Inject constructor(
    private val getCocktailsUseCase: GetCocktailsUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun cringeLambda() {
        viewModelScope.launch {
            getCocktailsUseCase().collect { cocktails ->
                _state.update {
                    it.copy(
                        cocktailList  = cocktails
                    )
                }
            }
        }
    }

    fun basedLambda() {
        viewModelScope.launch getCocktailsUseCase().collect _state.update { cocktails ->
            it.copy(
                cocktailList  = cocktails
            )
        }
    }

    fun lambdaWithSeveralLinesWithoutBrackets() {
        viewModelScope.launch
        getCocktailsUseCase().collect
        _state.update { cocktails ->
            it.copy(
                cocktailList  = cocktails
            )
        }
    }

    fun cringe() {
        if(true) {
            if(true) {
                if(true) {
                    if(true) {
                        val a = 1 + 1
                    }
                }
            }
        }
    }

    fun based() {
        if(true) if(true) if(true) if(true) {
            val a = 1 + 1
        }
    }

    fun orSeveralLines() {
        if(true)
        if(true)
        if(true)
        if(true) {
            val a = 1 + 1
        }
    }
}