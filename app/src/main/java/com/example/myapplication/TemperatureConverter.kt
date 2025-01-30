package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class TemperatureConverter : AppCompatActivity() {
    // Logging tag for debugging
    private val TAG = "JetpackComposeUI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display for a modern look
        enableEdgeToEdge()
        // Set the content of the screen using Jetpack Compose
        setContent {
            TemperatureConverterScreen()
        }
    }

    // Converts Fahrenheit to Celsius
    private fun fahrenheitToCelsius(f: Float): Float {
        return (f - 32) * 5f / 9f
    }

    // Converts Celsius to Fahrenheit
    private fun celsiusToFahrenheit(c: Float): Float {
        return (c * 9f / 5f) + 32f
    }

    @Preview(showBackground = true)
    @Composable
    fun TemperatureConverterScreen() {
        // State variables for temperature, result, and unit selection
        var temperature by remember { mutableStateOf(0f) }
        var result by remember { mutableStateOf(0f) }
        var isFahrenheit by remember { mutableStateOf(false) }

        // Main layout using a Column
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            // TextField for temperature input
            TextField(
                value = temperature.toString(),
                onValueChange = { newTemp ->
                    temperature = newTemp.toFloatOrNull() ?: temperature // Update temperature state
                },
                label = { Text("Digite temperatura") }, // Label for the TextField
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Set keyboard type to number
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical spacing

            // Row for unit selection (Celsius/Fahrenheit)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "C") // Celsius label
                Switch(checked = isFahrenheit, onCheckedChange = { checked -> isFahrenheit = checked }) // Unit switch
                Text(text = "F") // Fahrenheit label
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add vertical spacing

            // Button to trigger the conversion
            Button(onClick = {
                // Perform the temperature conversion
                result = if (isFahrenheit) {
                    fahrenheitToCelsius(temperature)
                } else {
                    celsiusToFahrenheit(temperature)
                }
                Log.d(TAG, "button click $result") // Log the result
            }) {
                Text(text = "convert") // Button text
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add vertical spacing

            // Display the converted result
            Text(text = String.format("%.2f", result))
        }
    }
}