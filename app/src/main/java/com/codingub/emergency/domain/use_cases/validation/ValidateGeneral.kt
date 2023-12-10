package com.codingub.emergency.domain.use_cases.validation

import javax.inject.Inject


class ValidateGeneral @Inject constructor(){
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
