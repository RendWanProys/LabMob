package com.example.moblabs.lab2.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moblabs.R
import com.example.moblabs.lab2.components.DotsMenuIcon
import com.example.moblabs.lab2.components.MapPin
import com.example.moblabs.ui.theme.MichromaFont

@Composable
fun PetSearchScreen(
    onBack: () -> Unit,
    onFindPets: () -> Unit
) {
    var cityText by remember { mutableStateOf("") }
    var selectedDistance by remember { mutableStateOf("100 miles") }
    var isDropdownOpen by remember { mutableStateOf(false) }
    val distanceOptions = listOf("25 miles", "50 miles", "100 miles", "250 miles", "500 miles")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8C4C16))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF9E5418),
                                Color(0xFF884311),
                                Color(0xFF6B3107)
                            )
                        )
                    )
            )

            Image(
                painter = painterResource(id = R.drawable.map_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                DotsMenuIcon(tint = Color(0xFFFA8B28))
            }

            Text(
                text = "FIND THE RIGHT DOG FOR YOU",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MichromaFont,
                color = Color(0xFFFFD180),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 46.dp)
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 70.dp, top = 200.dp),
                size = 28.dp,
                alpha = 0.85f,
                onClick = onFindPets
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 90.dp, top = 150.dp),
                size = 28.dp,
                alpha = 0.85f,
                onClick = onFindPets
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 45.dp, top = 310.dp),
                size = 26.dp,
                alpha = 0.82f,
                onClick = onFindPets
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 50.dp, top = 460.dp),
                size = 28.dp,
                alpha = 0.85f,
                onClick = onFindPets
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 80.dp, top = 420.dp),
                size = 28.dp,
                alpha = 0.85f,
                onClick = onFindPets
            )

            MapPin(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 250.dp),
                size = 54.dp,
                alpha = 1.0f,
                hasShadow = true,
                onClick = onFindPets
            )
        }

        Card(
            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Text(
                    text = "Let's get this right",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MichromaFont,
                    color = Color(0xFF263238),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .border(1.dp, Color(0xFFE8DCD2), RoundedCornerShape(26.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MapPin(
                            size = 14.dp,
                            color = Color(0xFFF27B2B),
                            hasShadow = false
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (cityText.isEmpty()) {
                                Text(
                                    text = "Enter City, State, or ZIP",
                                    fontSize = 13.sp,
                                    fontFamily = MichromaFont,
                                    color = Color(0xFFB0A8A0)
                                )
                            }
                            BasicTextField(
                                value = cityText,
                                onValueChange = { cityText = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = MichromaFont,
                                    color = Color(0xFF263238)
                                ),
                                cursorBrush = SolidColor(Color(0xFFF27B2B)),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (cityText.isNotEmpty()) {
                            Text(
                                text = "✕",
                                fontSize = 14.sp,
                                color = Color(0xFF9E9E9E),
                                modifier = Modifier
                                    .clickable { cityText = "" }
                                    .padding(4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .border(1.dp, Color(0xFFE8DCD2), RoundedCornerShape(26.dp))
                        .background(Color.White)
                        .animateContentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clickable { isDropdownOpen = !isDropdownOpen }
                            .padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Distance",
                                fontSize = 11.sp,
                                fontFamily = MichromaFont,
                                color = Color(0xFFB0A8A0)
                            )
                            Text(
                                text = selectedDistance,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = MichromaFont,
                                color = Color(0xFFF27B2B)
                            )
                        }
                        Text(
                            text = if (isDropdownOpen) "▲" else "▼",
                            fontSize = 11.sp,
                            color = Color(0xFFF27B2B)
                        )
                    }

                    if (isDropdownOpen) {
                        HorizontalDivider(color = Color(0xFFF5EDE6), thickness = 1.dp)
                        distanceOptions.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDistance = option
                                        isDropdownOpen = false
                                    }
                                    .padding(horizontal = 18.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 13.sp,
                                    fontFamily = MichromaFont,
                                    fontWeight = if (option == selectedDistance) FontWeight.Bold else FontWeight.Normal,
                                    color = if (option == selectedDistance) Color(0xFFF27B2B) else Color(0xFF263238)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onFindPets,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A3110)),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "FIND PETS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MichromaFont,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    PetSearchScreen(onBack = {}, onFindPets = {})
}