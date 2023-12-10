package com.codingub.emergency.domain.use_cases.validation

import javax.inject.Inject

class ValidateBirthday @Inject constructor() {
    fun execute(isDateValid: Boolean): ValidationResult {

        return if (!isDateValid) {
            ValidationResult(
                successful = false,
                errorMessage = "Birthday is not valid"
            )
        } else {
            ValidationResult(successful = true)
        }
    }
}
