package com.example.moblabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.moblabs.lab1.FlippableRecipeCard
import com.example.moblabs.lab2.PetfinderApp
import com.example.moblabs.menu.MenuScreen
import com.example.moblabs.ui.theme.MobLabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobLabsTheme(dynamicColor = false) {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf("menu") }

    BackHandler(enabled = currentScreen != "menu") {
        currentScreen = "menu"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        when (currentScreen) {
            "menu" -> MenuScreen(
                onOpenLab1 = { currentScreen = "lab1" },
                onOpenLab2 = { currentScreen = "lab2" }
            )
            "lab1" -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FlippableRecipeCard()
                }
            }
            "lab2" -> {
                PetfinderApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppNavigator()
}
