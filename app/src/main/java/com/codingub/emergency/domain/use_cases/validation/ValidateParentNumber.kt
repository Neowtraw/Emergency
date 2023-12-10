package com.codingub.emergency.domain.use_cases.validation

import android.util.Patterns

class ValidateParentNumber {
    fun execute(phone: String, age: Int): ValidationResult {

        if(age == -1) {
            return ValidationResult(
                successful = false
            )
        }
        if(age < 18 && phone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone can't be empty"
            )
        }
        if (phone.length < 11) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone can't be shorter than 11 symbols"
            )
        }
        if(!Patterns.PHONE.matcher(phone).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "This phone is not valid"
            )
        }
        return ValidationResult(successful = true)

    }
}