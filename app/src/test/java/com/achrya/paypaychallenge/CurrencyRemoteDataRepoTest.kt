import com.achrya.paypaychallenge.data.entity.RateEntity
import com.achrya.paypaychallenge.data.network.CurrencyService
import com.achrya.paypaychallenge.data.repoimpl.CurrencyRemoteDataRepoImpl
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.data.entity.CurrencyEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotEquals

@RunWith(MockitoJUnitRunner::class)
class CurrencyRemoteDataRepoTest {

    private lateinit var currencyRemoteDataRepo: CurrencyRemoteDataRepo

    @Mock
    private lateinit var currencyService: CurrencyService

    @Before
    fun setup() {
        currencyRemoteDataRepo = CurrencyRemoteDataRepoImpl(currencyService)
    }

    @Test
    fun getCurrencyList_returnsCorrectValues() = runTest {
        val appId = "testAppId"
        val currencyEntity = CurrencyEntity("disclaimer", "license", 100, "base",
        listOf(RateEntity("USD", 1.0f), RateEntity("EUR", 2.0f))
        )
        Mockito.`when`(currencyService.getCurrencyList(appId)).thenReturn(flowOf(currencyEntity))

        val result = currencyRemoteDataRepo.getCurrencyList(appId).toList().first()

        assertEquals("disclaimer", result.disclaimer)
        assertEquals("license", result.license)
        assertEquals(100L, result.timestamp)
        assertEquals("base", result.base)
        assertEquals(2, result.rates.size)
        assertEquals("USD", result.rates[0].currency)
        assertEquals(1.0f, result.rates[0].exchangeRate)
        assertEquals("EUR", result.rates[1].currency)
        assertEquals(2.0f, result.rates[1].exchangeRate)
        assertNotEquals(3, result.rates.size)
    }
}