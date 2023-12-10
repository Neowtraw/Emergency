package com.codingub.emergency.domain.use_cases.validation

import android.util.Log


class ValidateGeneral {
    fun execute(str: String) : ValidationResult {
        if(str.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Can't be blank :("
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
