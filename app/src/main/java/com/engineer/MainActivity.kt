package com.engineer

import ToeGame
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.engineer.ui.theme.EngineerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EngineerTheme {
                ToeGame()
            }

        }
    }
}









