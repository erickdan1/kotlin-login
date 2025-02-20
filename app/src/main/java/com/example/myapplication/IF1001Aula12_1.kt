package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class IF1001Aula12_1: AppCompatActivity() {

    val LOG: String = "IF1001Aula12_1"
    lateinit var roomDB_userDetails: RoomDB_UserDetails


    // Arquivo de preferências
    val preferences: DataStore<Preferences> by preferencesDataStore(name = "Login")

    // Chaves
    val usernameKey: Preferences.Key<String> = stringPreferencesKey("username")
    val pwdKey: Preferences.Key<String> = stringPreferencesKey("pwd")
    val rememberKey: Preferences.Key<Boolean> = booleanPreferencesKey("remember")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyUI()
        }

        roomDB_userDetails = Room.databaseBuilder(
            applicationContext,
            RoomDB_UserDetails::class.java,
            "user_details_db"
        ).build()

    }

    val users: List<RoomEntity_UserDetails> = roomDB_userDetails.userDao().getAll()

    suspend fun writePreferences(username: String, password: String, remember: Boolean) {
        preferences.edit {
            it[usernameKey] = username
            it[pwdKey] = password
            it[rememberKey] = remember
        }
    }

    suspend fun readPreferences(): Triple<String, String, Boolean> {
        val result = preferences.data.catch {
            if (it is IOException) {
                Log.d(LOG, "aasdf")
            }
        }.map {
            val resultUsername = it[usernameKey] ?: ""
            val resultPassword = it[pwdKey] ?: ""
            val resultRemember = it[rememberKey] ?: false

            Triple(resultUsername, resultPassword, resultRemember)
        }.first()

        return result
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

        LaunchedEffect(Unit) {
            val result = readPreferences()

            text_username = result.first
            text_password = result.second
            remember_me = result.third
        }

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
//                        Log.d(LOG, "valid login")
//                        val filename: String = "login"
//                        val content: String =
//                            text_username + ";" + text_password + ";" + remember_me
//
//                        applicationContext.openFileOutput(filename, MODE_PRIVATE).use({
//                            it.write(content.toByteArray())
//                        })
//
//                        Log.d(LOG, content)
                        lifecycleScope.launch {
                            writePreferences(text_username, text_password, remember_me)
                        }

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