package kz.nkaiyrken.newsapp2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.nkaiyrken.newsapp2025.presentation.MainScreen
import kz.nkaiyrken.newsapp2025.ui.theme.NewsApp2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NewsApp2025Theme {
                MainScreen()
            }
        }
    }
}

