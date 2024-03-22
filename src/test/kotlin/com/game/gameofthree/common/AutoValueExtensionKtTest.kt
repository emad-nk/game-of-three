package com.game.gameofthree.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AutoValueExtensionKtTest {

    @ParameterizedTest
    @CsvSource(
        "56, 1",
        "3, 0",
        "7, -1",
        "2, 1",
    )
    fun `finds the best value to add to the number which divides by 3`(value: Int, bestValue: Int) {
        assertThat(findBestDivisibleBy3(number = value)).isEqualTo(bestValue)
    }

    @Test
    fun `gets a random number between 2 to 1000`() {
        assertThat(getARandomNumberBetweenTwoAndThousand()).isBetween(2, 1000)
    }
}
