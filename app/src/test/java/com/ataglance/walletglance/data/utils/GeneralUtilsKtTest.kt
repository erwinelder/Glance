package com.ataglance.walletglance.data.utils

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GeneralUtilsKtTest {

    companion object {
        @JvmStatic
        fun formatWithSpacesDataProvider(): Collection<Arguments> {
            return listOf(
                Arguments.of(1.89),
                Arguments.of(12.89),
                Arguments.of(123.89),
                Arguments.of(1234.89),
                Arguments.of(12345.89),
                Arguments.of(123456.89),
                Arguments.of(1234567.89),
                Arguments.of(1.0)
            )
        }
    }

    @ParameterizedTest
    @MethodSource("formatWithSpacesDataProvider")
    fun formatWithSpaces(number: Double) {
//        Assertions.assertEquals(number.formatWithSpaces(), number.formatWithSpacesNew())
    }
}