package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.core.Country
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.models.User
import com.codingub.emergency.domain.use_cases.GetServicesFromLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getServicesFromLanguage: GetServicesFromLanguage,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel(){

    private val _country: MutableStateFlow<Country> = MutableStateFlow(Country.Belarus)
    val country: StateFlow<Country> = _country.asStateFlow()

    private val _services: MutableStateFlow<ResultState<List<Service>>> = MutableStateFlow(
        ResultState.Loading())
    val services: StateFlow<ResultState<List<Service>>> = _services.asStateFlow()

    private lateinit var user: User
    fun getUser() : User = user

    init{
        viewModelScope.launch {
            dataStoreRepository.readLanguage().collectLatest {
                setCountry(it)
            }
        }
        viewModelScope.launch {
            dataStoreRepository.readUserInfo().collectLatest {
                user = it
            }
        }
    }

    private fun getServices() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getServicesFromLanguage(_country.value.language)
            data.collect{ sers ->
                _services.value = sers
            }
        }
    }

    fun setCountry(c: Country) {
        viewModelScope.launch {
            dataStoreRepository.saveLanguage(c)
        }
        _country.value = c
        getServices()
    }

}