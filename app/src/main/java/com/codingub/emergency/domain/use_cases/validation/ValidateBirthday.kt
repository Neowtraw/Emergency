package com.codingub.emergency.domain.use_cases.validation

class ValidateBirthday {
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
