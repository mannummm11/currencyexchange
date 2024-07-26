package com.achrya.paypaychallenge.presenter.currency_calc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import com.achrya.paypaychallenge.ui.theme.PaypaychallengeTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    /*private val currencyRemoteDataRepo by inject<CurrencyRemoteDataRepo>()
    private val currencyStorageDataRepo: CurrencyStorageDataRepo by inject()
    private val currencyPreferenceRepo: CurrencyPreferenceRepo by inject()*/
    private val viewModel: CurrencyCalculatorViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaypaychallengeTheme {
                LaunchedEffect(key1 = true) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        val data = viewModel.getRefreshData()
                        Log.d("db data", data.toString())
                    }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MaterialTheme(

                    ) {
                        CurrencyExchangeView()
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyExchangeView() {
    val viewModel: CurrencyCalculatorViewModel = getViewModel()
    TopRow()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRow() {
    var expanded by remember { mutableStateOf(false) }
    val dropdownItems = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem by remember { mutableStateOf(dropdownItems[0]) }
    var textFieldValue by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,

    ) {
        TextField(
            value = textFieldValue,
            onValueChange = {textFieldValue = it},
        placeholder = { Text("Text Field") }
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                readOnly = true,
                value = selectedItem,
                onValueChange = {selectedItem = it},
                trailingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                dropdownItems.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            selectedItem = it
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}