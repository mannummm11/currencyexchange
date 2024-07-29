import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.usecase.GetCalculatedCurrencyDetail
import com.achrya.paypaychallenge.utils.ApiStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCalculatedCurrencyDetailTest {

    @Test
    fun `invoke correctly calculates exchange rates`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 1.0f)
        val moneyUnit = 100f
        val rates = listOf(
            Rate("EUR", 0.85f),
            Rate("GBP", 0.75f),
            Rate("USD", 1.0f)
        )
        val getCalculatedCurrencyDetail = GetCalculatedCurrencyDetail()

        // Act
        val result = getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates).toList()

        // Assert
        val expectedRates = listOf(
            Rate("EUR", 85f),
            Rate("GBP", 75f),
            Rate("USD", 100.0f)
        )
        //val expectedCurrency = Currency(0, "USD", expectedRates)
       assertEquals(result[2].data?.rates, expectedRates)
        assertEquals(result[2].data?.rates?.size, 3)
    }

    @Test
    fun `invoke correctly calculates exchange rates when amount is less then or equal zero`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 1.0f)
        val moneyUnit = 0f
        val rates = listOf(
            Rate("EUR", 0.85f),
            Rate("GBP", 0.75f),
            Rate("USD", 1.0f)
        )
        val getCalculatedCurrencyDetail = GetCalculatedCurrencyDetail()

        // Act
        val result = getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates).toList()

        //val expectedCurrency = Currency(0, "USD", expectedRates)
        assertEquals(result[0].status, ApiStatus.LOADING)
        assertEquals(result[1].status, ApiStatus.ERROR)
        assertEquals(result[1].message, "Money unit should be greater than zero")
    }

    @Test
    fun `invoke correctly calculates exchange rates when exchange rate is less then or equal zero`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 1.0f)
        val moneyUnit = 100f
        val rates = listOf(
            Rate("EUR", 0.0f),
            Rate("GBP", 0.75f),
            Rate("USD", 1.0f)
        )
        val getCalculatedCurrencyDetail = GetCalculatedCurrencyDetail()

        // Act
        val result = getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates).toList()

        //val expectedCurrency = Currency(0, "USD", expectedRates)
        assertEquals(result[0].status, ApiStatus.LOADING)
        assertEquals(result[1].status, ApiStatus.LOADING)
        assertEquals(result[2].message, "Exchange rate should be greater than zero")
    }
}