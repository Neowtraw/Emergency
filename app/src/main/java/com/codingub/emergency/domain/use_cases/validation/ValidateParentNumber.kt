package com.codingub.emergency.domain.use_cases.validation

import android.util.Patterns
import javax.inject.Inject

class ValidateParentNumber @Inject constructor() {
    fun execute(phone: String, age: Int): ValidationResult {

        if (age == -1) {
            return ValidationResult(
                successful = false
            )
        }

        if (age < 18 && (phone.isBlank() || !Patterns.PHONE.matcher(phone).matches())) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone must be provided and valid for users under 18"
            )
        } else if (age >= 18 && phone.isNotBlank() && !Patterns.PHONE.matcher(phone).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone provided is not valid"
            )
        }
        return ValidationResult(successful = true)

    }
}