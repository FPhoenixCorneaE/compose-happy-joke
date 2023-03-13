package com.fphoenixcorneae.happyjoke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fphoenixcorneae.happyjoke.ui.page.SplashScreen
import com.fphoenixcorneae.happyjoke.ui.theme.ComposeHappyJokeTheme

/**
 * @desc：
 * @date：2023/03/09 13:39
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHappyJokeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SplashScreen(window = window)
                }
            }
        }
    }
}