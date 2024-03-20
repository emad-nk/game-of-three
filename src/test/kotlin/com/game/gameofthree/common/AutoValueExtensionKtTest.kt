package com.game.gameofthree.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AutoValueExtensionKtTest {

    @ParameterizedTest
    @CsvSource(
        "56, 57",
        "3, 3",
        "7, 6",
        "2, 3"
    )
    fun `finds the best number which divides by 3`(value: Int, bestValue: Int){
        assertThat(findBestDivisibleBy3(number = value)).isEqualTo(bestValue)
    }
}
