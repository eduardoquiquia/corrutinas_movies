package com.tecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.ui.movie.MovieScreen
import com.tecsup.ui.theme.Corrutinas_moviesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Corrutinas_moviesTheme {
                MovieScreen()
            }
        }
    }
}
