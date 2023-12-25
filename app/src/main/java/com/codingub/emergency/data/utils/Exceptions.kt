package com.codingub.emergency.data.utils

import com.codingub.emergency.R
import com.codingub.emergency.core.Resource


// Network
class NetworkLostException(private val resources: Resource) : Throwable() {

    override val message: String
        get() = resources.string(R.string.exception_network_lost)
}

// No Results
class NoResultsException(private val resources: Resource) : Throwable() {

    override val message: String
        get() = resources.string(R.string.exception_no_results)
}

// Unknown Error
class UnknownErrorException(private val resources: Resource) : Throwable() {

    override val message: String
        get() = resources.string(R.string.exception_unknown_error)
}