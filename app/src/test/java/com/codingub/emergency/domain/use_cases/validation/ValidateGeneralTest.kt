package com.codingub.emergency.domain.use_cases.validation

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ValidateGeneralTest {

    @Test
    fun `when user puts info if is not blank expect success`() {
        // initialize
        val validateGeneral  = ValidateGeneral()
        val str = "test-str"
        val expectedResult = ValidationResult(true)

        // when
        val data = validateGeneral.execute(str)

        // then
        assertThat(data).isEqualTo(expectedResult)
    }

    @Test
    fun `when user puts a valid birthday if is blank expect error`() {
        val validateGeneral  = ValidateGeneral()
        val str = ""
        val expectedResult = ValidationResult(false, "Can't be blank :(")

        // when
        val data = validateGeneral.execute(str)

        // then
        assertThat(data).isEqualTo(expectedResult)
    }
}