package com.codingub.emergency.domain.use_cases.validation

import com.MockitoHelper.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ValidateBirthdayTest {

    @Mock
    lateinit var validateBirthday: ValidateBirthday

    @get:Rule(order = 0)
    var mockRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        Mockito.validateMockitoUsage()
    }

    @Test
    fun `when user puts a valid birthday, expect success`() {
        // initialize
        val expectedResult = ValidationResult(successful = true)
        whenever(validateBirthday.execute(true)).thenReturn(expectedResult)

        // when
        val result = validateBirthday.execute(true)

        // then
        assertThat(expectedResult).isEqualTo(result)
        verify(validateBirthday).execute(true)
    }

    @Test
    fun `when user puts an invalid birthday, expect error`() {
        // initialize
        val expectedResult =
            ValidationResult(successful = false, errorMessage = "Birthday is nt valid")
        whenever(validateBirthday.execute(false)).thenReturn(expectedResult)

        // when
        val result = validateBirthday.execute(false)

        // then
        assertThat(expectedResult).isEqualTo(result)
        verify(validateBirthday).execute(false)
    }
}