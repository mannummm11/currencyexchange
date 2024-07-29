import com.achrya.paypaychallenge.BuildConfig
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.data.entity.RateEntity
import com.achrya.paypaychallenge.data.mapper.toCurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail
import com.achrya.paypaychallenge.utils.ApiStatus
import com.achrya.paypaychallenge.utils.NetworkResult
import com.example.example.CurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLatestCurrencyDetailTest {

    private lateinit var getLatestCurrencyDetail: GetLatestCurrencyDetail

    @Mock
    private lateinit var currencyRemoteDataRepo: CurrencyRemoteDataRepo

    @Mock
    private lateinit var currencyStorageDataRepo: CurrencyStorageDataRepo

    @Mock
    private lateinit var currencyPreferenceRepo: CurrencyPreferenceRepo

    @Before
    fun setup() {
        getLatestCurrencyDetail = GetLatestCurrencyDetail(currencyRemoteDataRepo, currencyStorageDataRepo, currencyPreferenceRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke_returnsCorrectValues when time is less then 30 minutes`() = runTest {
        val rates = listOf(CurrencyExchangeTable("USD", 1.0f),CurrencyExchangeTable("AED", 2.0f))
        val networkResult = NetworkResult.Success(rates)

        Mockito.`when`(currencyStorageDataRepo.getAllCurrenciesFromDB()).thenReturn(flowOf(rates))
        Mockito.`when`(currencyPreferenceRepo.getTimeStampAndBase()).thenReturn(Pair(System.currentTimeMillis()-(1000*60*10), "USD"))

        val result = getLatestCurrencyDetail.invoke().toList()

        assertEquals(listOf(networkResult)[0]._data?.size, result[2].data?.rates?.size)
        assertEquals(result[1].status, ApiStatus.LOADING)
    }

    @Test
    fun `invoke_returnsCorrectValues when time is greater then 30 minutes`() = runTest {
        val rates = listOf(Rate("USD", 1.0f))
        val currency = Currency(0L, "USD", rates)
        val currencyEntity = CurrencyEntity("disclaimer", "license", 0L, "base", listOf(RateEntity("USD", 1.0f)))
        val networkResult = NetworkResult.Success(currency)

        Mockito.`when`(currencyRemoteDataRepo.getCurrencyList(BuildConfig.APP_ID)).thenReturn(flowOf(currencyEntity))
        Mockito.`when`(currencyPreferenceRepo.getTimeStampAndBase()).thenReturn(Pair(System.currentTimeMillis()-(1000*60*50), "USD"))
        Mockito.`when`(currencyStorageDataRepo.getAllCurrenciesFromDB()).thenReturn(flowOf(currencyEntity.toCurrencyExchangeTable().allCurrencyExchange))

        val result = getLatestCurrencyDetail.invoke().toList()

        assertEquals(listOf(networkResult)[0]._data?.rates?.size, result[2].data?.rates?.size)
        assertEquals(result[1].status, ApiStatus.LOADING)
    }

    @Test
    fun `invoke_returnsCorrectValues when local db does not have data`() = runTest {
        val rates = listOf(Rate("USD", 1.0f))
        val currency = Currency(0L, "USD", rates)
        val currencyEntity = CurrencyEntity("disclaimer", "license", 0L, "base", listOf(RateEntity("USD", 1.0f)))
        val networkResult = NetworkResult.Success(currency)

        Mockito.`when`(currencyRemoteDataRepo.getCurrencyList(BuildConfig.APP_ID)).thenReturn(flowOf(currencyEntity))
        Mockito.`when`(currencyPreferenceRepo.getTimeStampAndBase()).thenReturn(Pair(System.currentTimeMillis()-(1000*60*50), "USD"))
        Mockito.`when`(currencyStorageDataRepo.getAllCurrenciesFromDB()).thenReturn(null)

        val result = getLatestCurrencyDetail.invoke().toList()
        assertEquals(result[2].status, ApiStatus.ERROR)
        assertEquals(result[1].status, ApiStatus.LOADING)
    }

}