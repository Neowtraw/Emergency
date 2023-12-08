package com.codingub.emergency.domain.models

import androidx.annotation.StringRes
import com.codingub.emergency.R

enum class Call(@StringRes val title: Int, @StringRes val phone: Int) {
    MES(R.string.mes, R.string.phone_mes),
    POLICE(R.string.police, R.string.phone_police),
    AMBULANCE(R.string.ambulance, R.string.phone_ambulacne),
    FIRE_DEPARTMENT(R.string.fire_department, R.string.phone_fire_department),
    GAS_SERVICE(R.string.gas_service, R.string.phone_gas_service)
}