package com.example.rad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.rad.algorithm.Algorithm
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.algorithm.AlgorithmViewModelProvider
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.chatgpt.OPENAI_API_KEY
import com.example.rad.database.DatabaseViewModel
import com.example.rad.main.MainApp
import io.github.cdimascio.dotenv.dotenv

class MainActivity : ComponentActivity() {
    private val viewModel: AlgorithmViewModel by lazy {
        val viewModelProviderFactory = AlgorithmViewModelProvider(Algorithm())
        ViewModelProvider(this, viewModelProviderFactory)[AlgorithmViewModel::class.java]
    }

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val databaseViewModel: DatabaseViewModel by lazy {
        ViewModelProvider(this)[DatabaseViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val dotenv = dotenv {
            directory = "./assets"
            filename = "env"
        }
        OPENAI_API_KEY = dotenv["OPENAI_API_KEY"]

        setContent {
            // this.deleteDatabase("algorithm_database")
            MainApp(
                databaseViewModel = databaseViewModel,
                viewModel = viewModel,
                chatViewModel = chatViewModel
            )
        }
    }
}
