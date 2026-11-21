package com.example.moblabs.lab2

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.moblabs.lab2.screens.PetDetailScreen
import com.example.moblabs.lab2.screens.PetfinderHomeScreen
import com.example.moblabs.lab2.screens.PetSearchScreen

@Composable
fun PetfinderApp() {
    var activeScreen by remember { mutableStateOf("home") }

    BackHandler(enabled = activeScreen != "home") {
        activeScreen = "home"
    }

    when (activeScreen) {
        "home" -> PetfinderHomeScreen(
            onOpenSearch = { activeScreen = "search" },
            onOpenDetails = { activeScreen = "details" }
        )
        "search" -> PetSearchScreen(
            onBack = { activeScreen = "home" },
            onFindPets = { activeScreen = "details" }
        )
        "details" -> PetDetailScreen(
            onBack = { activeScreen = "home" }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PetfinderAppPreview() {
    PetfinderApp()
}
