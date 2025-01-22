package com.senijoshua.pods.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.senijoshua.pods.presentation.theme.PodsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PodsTheme {
                PodsRoot()
            }
        }
    }
}
