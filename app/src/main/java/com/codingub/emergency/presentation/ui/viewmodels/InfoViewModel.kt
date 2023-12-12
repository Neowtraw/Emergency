package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.common.Country
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.use_cases.GetServicesFromLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getServicesFromLanguage: GetServicesFromLanguage
) : ViewModel(){

    private val _country: MutableStateFlow<Country> = MutableStateFlow(Country.Belarus)
    val country: StateFlow<Country> = _country.asStateFlow()

    private val _services: MutableStateFlow<ResultState<List<Service>>> = MutableStateFlow(
        ResultState.Loading())
    val services: StateFlow<ResultState<List<Service>>> = _services.asStateFlow()

    init{
        getServices()
    }

    fun getServices() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getServicesFromLanguage(_country.value.language)
            data.collect{ sers ->
                _services.value = sers
            }
        }
    }

    fun setCountry(c: Country) {
        _country.value = c
        getServices()
    }

}