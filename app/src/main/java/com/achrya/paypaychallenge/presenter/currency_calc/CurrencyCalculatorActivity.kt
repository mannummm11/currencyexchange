package com.achrya.paypaychallenge.presenter.currency_calc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.ui.theme.PaypaychallengeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaypaychallengeTheme {
                PaypayAssignment()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaypayAssignment() {
        MaterialTheme {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        val navController = rememberNavController()
        val viewModel: CurrencyCalculatorViewModel = koinViewModel()
        val coroutineScope = rememberCoroutineScope()
        var selectedCurrency by rememberSaveable { mutableStateOf("Select Currency") }
        var isVisibility by remember { mutableStateOf(false) }
        DisposableEffect(key1 = navController.currentBackStackEntry) {
            val callback = NavController.OnDestinationChangedListener { _, destination, _ ->
                if (destination.route == "CurrencyExchangeUI" && !isVisibility) {
                    coroutineScope.launch {
                        isVisibility = true
                        viewModel.getRefreshData()
                    }
                }
            }
            navController.addOnDestinationChangedListener(callback)
            onDispose {
                navController.removeOnDestinationChangedListener(callback)
            }
        }

        NavHost(navController, startDestination = "CurrencyExchangeUI") {
            composable("CurrencyExchangeUI") {
                CurrencyExchangeUI(
                    navController,
                    viewModel = viewModel,
                    coroutineScope,
                    selectedCurrency
                )
            }
            composable("CurrencySelectorView") {
                CurrencySelectorView(viewModel) { rate ->
                    navController.popBackStack()
                    coroutineScope.launch {
                        selectedCurrency = rate.currency
                        viewModel.getCalculatedData(rate, viewModel.currencyUiState.currDetail, 1f)
                    }
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun IndeterminateProgressBar() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CurrencyExchangeUI(
        navController: NavController,
        viewModel: CurrencyCalculatorViewModel,
        coroutineScope: CoroutineScope,
        selectedCurrency: String
    ) {
        val uiState = viewModel.currencyUiState
        if (viewModel.currencyUiState.showProgressBar) IndeterminateProgressBar()
        else if (viewModel.currencyUiState.showError) ShowErrrorDialogBox()
        val expanded by remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(modifier = Modifier.background(color = Color.LightGray)) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.amount,
                    onValueChange = { if (it.length <= 8) viewModel.updateAmountField(it.trim()) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .background(Color.White),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),

                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.amount.let { it.contains("-") || it.contains(",") || it.toFloat() <= 0 }) uiState.amount =
                                "1"
                            coroutineScope.launch(Dispatchers.IO) {
                                val basRate =
                                    uiState.currDetail.find { it.currency == selectedCurrency }
                                basRate?.let {
                                    viewModel.getCalculatedData(
                                        it,
                                        uiState.currDetail,
                                        uiState.amount.toFloat()
                                    )
                                }
                            }
                            keyboardController?.hide()
                        }
                    )
                )

                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        OutlinedTextField(
                            value = selectedCurrency,
                            onValueChange = {},
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier
                        .matchParentSize()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("CurrencySelectorView")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(uiState.currDetail.size) {
                    RoundedCardView(rate = uiState.currDetail[it])
                }
            }
        }
    }

    @Composable
    fun CurrencySelectorView(
        viewModel: CurrencyCalculatorViewModel,
        onItemSelected: (Rate) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray), contentAlignment = Alignment.Center
        ) {
            LazyColumn(userScrollEnabled = true, modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                items(viewModel.currencyUiState.currDetail.size) { item ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .height(IntrinsicSize.Min)
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Cyan
                        )
                    ) {
                        Text(
                            text = viewModel.currencyUiState.currDetail[item].currency,
                            modifier = Modifier
                                .clickable { onItemSelected(viewModel.currencyUiState.currDetail[item]) }
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }


            }
        }
    }

    @Composable
    fun RoundedCardView(rate: Rate) {
        val color = Color.Cyan
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Cyan
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = rate.currency,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = rate.exchangeRate.toString(),
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }

    @Composable
    fun ShowErrrorDialogBox() {
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = CONNECTIVITY_HEADER)
                },
                text = {
                    Text(CONNECTIVITY_MSG)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(OK)
                    }
                },
            )
        }
    }
}

const val CONNECTIVITY_HEADER = "Connectivity Issue"
const val CONNECTIVITY_MSG = "Please check internet connection."
const val OK = "OK"