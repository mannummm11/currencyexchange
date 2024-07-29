import androidx.compose.runtime.collectAsState
import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDAO
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.data.repoimpl.CurrencyStorageRepoImpl
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
class CurrencyStorageRepoImplTest {

    @Mock
    private lateinit var currencyDAO: CurrencyDAO

    private lateinit var currencyStorageRepoImpl: CurrencyStorageDataRepo

    @Before
    fun setup() {
        currencyStorageRepoImpl = CurrencyStorageRepoImpl(currencyDAO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAllCurrencyToDb_insertsCorrectly() = runTest {
        val currencies = listOf(
            CurrencyExchangeTable("USD", 1.0f),
            CurrencyExchangeTable("EUR", 0.85f)
        )

        currencyStorageRepoImpl.insertAllCurrencyToDb(currencies)

        Mockito.verify(currencyDAO).insertAllCurrency(currencies)
    }

    @Test
    fun getAllCurrenciesFromDB_returnsCorrectCurrencies() = runBlocking {
        val currencies = listOf(
            CurrencyExchangeTable("USD", 1.0f),
            CurrencyExchangeTable("EUR", 0.85f)
        )

        currencyStorageRepoImpl.insertAllCurrencyToDb(currencies)

        val rates = listOf(
            CurrencyExchangeTable("USD", 1.0f),
            CurrencyExchangeTable("EUR", 0.85f)
        )

        Mockito.`when`(currencyDAO.getAllCurrency()).thenReturn(flowOf(currencies))

        val result = currencyStorageRepoImpl.getAllCurrenciesFromDB().toList().first()

        assertEquals(rates, result)
    }
}