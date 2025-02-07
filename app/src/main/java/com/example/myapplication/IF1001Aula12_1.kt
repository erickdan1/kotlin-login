package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.io.File

class IF1001Aula12_1: AppCompatActivity() {

    val LOG: String = "IF1001Aula12_1"
    val preferences: DataStore<Preferences> by preferencesDataStore(name = "Login")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyUI()
        }
    }

    fun isValidLogin(username: String, password: String): Boolean {

        if (username.isEmpty()) {
            return false
        } else if (password.isEmpty()) {
            return false
        }

        return true
    }

    fun GetSavedLogin(): Triple<String, String, Boolean> {

        val filename: String = "login"
        val content: String

        val file: File = File(applicationContext.filesDir, filename)

        if (!file.exists()) {
            return Triple("", "", false)
        }

        applicationContext.openFileInput(filename).bufferedReader().use {
            content = it.readLine()
        }

        Log.d(LOG, content)
        val result = content.split(";")

        return Triple(result[0], result[1],result[2].toBoolean())
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun MyUI() {

        var text_username by remember { mutableStateOf(GetSavedLogin().first) }
        var text_password by remember { mutableStateOf(GetSavedLogin().second) }
        var remember_me by remember { mutableStateOf(GetSavedLogin().third) }

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "IF1001 Aula 12")
            TextField(
                value = text_username,
                onValueChange = {text_username = it},
                label = {Text("username") })

            TextField(
                value = text_password,
                onValueChange = {text_password = it},
                label = {Text("password") })

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = remember_me,
                    onCheckedChange = {remember_me = it})
                Text(text = "remember me")
            }

            Button(onClick = {

                if (remember_me) {

                    if (isValidLogin(text_username, text_password)) {
                        Log.d(LOG, "valid login")
                        val filename: String = "login"
                        val content: String =
                            text_username + ";" + text_password + ";" + remember_me

                        applicationContext.openFileOutput(filename, MODE_PRIVATE).use({
                            it.write(content.toByteArray())
                        })

                        Log.d(LOG, content)

                    } else {
                        Log.d(LOG, "invalid login")
                    }
                } else {
                    val filename = "login"
                    val file: File = File(applicationContext.filesDir, filename)

                    file.delete()
                }

            }) {
                Text(text = "login")
            }
        }
    }
}