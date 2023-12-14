package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codingub.emergency.domain.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *  Uses for sharing article to Aticle
 */

class SharedViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _sharedState = MutableStateFlow("")
    val sharedState = _sharedState.asStateFlow()

    fun updateState(id: String) {
        _sharedState.value = id
    }
}

