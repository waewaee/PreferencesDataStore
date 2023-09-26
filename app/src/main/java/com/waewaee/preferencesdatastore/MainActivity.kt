package com.waewaee.preferencesdatastore

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //    private lateinit var dataStore: DataStore<Preferences>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnRead = findViewById<Button>(R.id.btnRead)
        val etKey = findViewById<EditText>(R.id.etKey)
        val etValue = findViewById<EditText>(R.id.etValue)
        val etReadKey = findViewById<EditText>(R.id.etReadKey)
        val tvResultValue = findViewById<TextView>(R.id.tvResultValue)

        btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                    etKey.text.toString(),
                    etValue.text.toString()
                )
            }
        }

        btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(etReadKey.text.toString())
                tvResultValue.text = value ?: "No value found"
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}