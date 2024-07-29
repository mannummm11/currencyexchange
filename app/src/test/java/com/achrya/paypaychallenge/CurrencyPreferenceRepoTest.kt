import android.content.SharedPreferences
import com.achrya.paypaychallenge.data.repoimpl.CurrencySharedPreferenceRepoImpl
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyPreferenceRepoTest {

    private lateinit var currencyPreferenceRepo: CurrencyPreferenceRepo
    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.putLong(Mockito.anyString(), Mockito.anyLong())).thenReturn(editor)
        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)
        currencyPreferenceRepo = CurrencySharedPreferenceRepoImpl(sharedPreferences)
    }

    @Test
    fun saveCurrentTimeStampAndBaseCurr_savesCorrectly() = runTest {
        val baseCurr = "USD"

        currencyPreferenceRepo.saveCurrentTimeStampAndBaseCurr(baseCurr, 100)

        Mockito.verify(editor).putLong(Mockito.anyString(), Mockito.anyLong())
        Mockito.verify(editor).putString(Mockito.anyString(), Mockito.anyString())
        Mockito.verify(editor).apply()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTimeStampAndBase_returnsCorrectValues() = runBlockingTest {
        Mockito.`when`(sharedPreferences.getLong(Mockito.anyString(), Mockito.anyLong())).thenReturn(0L)
        Mockito.`when`(sharedPreferences.getString(Mockito.anyString(), Mockito.anyString())).thenReturn("USD")

        val result = currencyPreferenceRepo.getTimeStampAndBase()

        assertEquals(Pair(0L, "USD"), result)
    }
}