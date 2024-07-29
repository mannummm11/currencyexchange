import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.usecase.GetCalculatedCurrencyDetail
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail
import com.achrya.paypaychallenge.presenter.currency_calc.CurrencyCalculatorViewModel
import com.achrya.paypaychallenge.presenter.currency_calc.CurrencyUiState
import com.achrya.paypaychallenge.utils.NetworkResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CurrencyCalculatorViewModelTest {

    @Mock
    private lateinit var getLatestCurrencyDetail: GetLatestCurrencyDetail

    @Mock
    private lateinit var getCalculatedCurrencyDetail: GetCalculatedCurrencyDetail

    private lateinit var viewModel: CurrencyCalculatorViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = CurrencyCalculatorViewModel(getLatestCurrencyDetail, getCalculatedCurrencyDetail)
    }

    @Test
    fun `getRefreshData updates currencyUiState based on NetworkResult Success`() = runTest {
        // Arrange
        val networkResult = NetworkResult.Success(Currency(0, "USD", listOf(Rate("EUR", 0.85f))))
        Mockito.`when`(getLatestCurrencyDetail.invoke()).thenReturn(flowOf(networkResult))

        // Act
        viewModel.getRefreshData()

        // Assert
        val expectedUiState = CurrencyUiState(
            amount = "1",
            baseCurr = "USD",
            currDetail = listOf(Rate("EUR", 0.85f)),
            showProgressBar = false,
            showError = false
        )
        assertEquals(expectedUiState, viewModel.currencyUiState)
    }

    @Test
    fun `getRefreshData updates currencyUiState based on NetworkResult Loading`() = runTest {
        // Arrange
        val networkResult = NetworkResult.Loading<Currency>(true)
        Mockito.`when`(getLatestCurrencyDetail.invoke()).thenReturn(flowOf(networkResult))

        // Act
        viewModel.getRefreshData()

        // Assert
        val expectedUiState = CurrencyUiState(
            showProgressBar = true,
        )
        assertEquals(expectedUiState.showProgressBar, viewModel.currencyUiState.showProgressBar)
    }

    @Test
    fun `getRefreshData updates currencyUiState based on NetworkResult Error`() = runTest {
        // Arrange
        val networkResult = NetworkResult.Error<Currency>("There was an error")
        Mockito.`when`(getLatestCurrencyDetail.invoke()).thenReturn(flowOf(networkResult))

        // Act
        viewModel.getRefreshData()

        // Assert
        val expectedUiState = CurrencyUiState(
            showError = true
        )
        assertEquals(expectedUiState.showError, viewModel.currencyUiState.showError)
    }

    @Test
    fun `getCalculatedData updates currencyUiState based on NetworkResult Success`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 1.0f)
        val moneyUnit = 100f
        val rates = listOf(Rate("EUR", 0.85f))
        val networkResult = NetworkResult.Success(Currency(0, "USD", listOf(Rate("EUR", 85f))))
        Mockito.`when`(getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates)).thenReturn(flowOf(networkResult))

        // Act
        viewModel.getCalculatedData(selectedCurrency, rates, moneyUnit)

        // Assert
        val expectedUiState = CurrencyUiState(
            amount = "100.0",
            baseCurr = "USD",
            currDetail = listOf(Rate("EUR", 85f)),
            showProgressBar = false,
            showError = false
        )
        assertEquals(expectedUiState, viewModel.currencyUiState)
        assertEquals(expectedUiState.amount, viewModel.currencyUiState.amount)
        assertEquals(expectedUiState.showProgressBar, viewModel.currencyUiState.showProgressBar)
    }

    @Test
    fun `getCalculatedData If Base Exchange is less then equal to 0`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 0f)
        val moneyUnit = 100f
        val rates = listOf(Rate("EUR", 0.85f))

        val networkResult = NetworkResult.Error<Currency>("error")
        Mockito.`when`(getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates)).thenReturn(flowOf(networkResult))

        viewModel.getCalculatedData(selectedCurrency, rates, moneyUnit)

        assertEquals(true, viewModel.currencyUiState.showError)
    }

    @Test
    fun `getCalculatedData If money unit or other currecy rate exchane Exchange is less then equal to 0`() = runTest {
        // Arrange
        val selectedCurrency = Rate("USD", 10f)
        val moneyUnit = 0f
        val rates = listOf(Rate("EUR", 0f))

        val networkResult = NetworkResult.Error<Currency>("error")
        Mockito.`when`(getCalculatedCurrencyDetail.invoke(selectedCurrency, moneyUnit, rates)).thenReturn(flowOf(networkResult))

        viewModel.getCalculatedData(selectedCurrency, rates, moneyUnit)

        assertEquals(true, viewModel.currencyUiState.showError)
    }
}