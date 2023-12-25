package com.codingub.emergency.presentation.ui.viewmodels

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.R
import com.codingub.emergency.core.Country
import com.codingub.emergency.core.Resource
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.data.utils.NetworkLostException
import com.codingub.emergency.data.utils.NoResultsException
import com.codingub.emergency.data.utils.UnknownErrorException
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.models.User
import com.codingub.emergency.domain.use_cases.GetServicesFromLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getServicesFromLanguage: GetServicesFromLanguage,
    private val dataStoreRepository: DataStoreRepository,
    private val resources: Resource
) : ViewModel(){

    private val _country: MutableStateFlow<Country> = MutableStateFlow(Country.Belarus)
    val country: StateFlow<Country> = _country.asStateFlow()

    private val _services: MutableStateFlow<ResultState<List<Service>>> = MutableStateFlow(
        ResultState.Loading())
    val services: StateFlow<ResultState<List<Service>>> = _services.asStateFlow()

    val serviceState = services
        .filter { !it.data.isNullOrEmpty() }
        .distinctUntilChanged()
        .map { result ->
            Log.d("test", result.toString())
            when (result) {
                is ResultState.Success -> {
                     ServiceState.Result(result.data!!)
                }
                is ResultState.Loading -> ServiceState.Loading
                is ResultState.Error -> {
                    if (result.error is NetworkErrorException  ||
                        result.error is UnknownHostException
                    ) ServiceState.NetworkLost(resources.string(R.string.exception_network_lost))
                    else ServiceState.Error(
                        result.error?.message ?: UnknownErrorException(resources).message
                    )
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ServiceState.Loading
        )

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

sealed class ServiceState {
    object Loading : ServiceState()
    data class Result(val services: List<Service>) : ServiceState()
    data class NetworkLost(val message: String) : ServiceState()
    data class Error(val message: String) : ServiceState()
}