package com.example.moblabs.lab1

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FlippableRecipeCard() {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "CardFlip"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .width(340.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
    ) {
        if (rotation <= 90f) {
            FrontCard(onOpenRecipe = { isFlipped = true })
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                BackCard(onBack = { isFlipped = false })
            }
        }
    }
}

@Composable
fun FrontCard(onOpenRecipe: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF81C784)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🏪",
                fontSize = 76.sp
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Паста Карбонара",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoBadge(text = "⭐ 4.8", bgColor = Color(0xFFFFF9C4))
                InfoBadge(text = "⏱ 30 мин", bgColor = Color(0xFFE8F5E9))
                InfoBadge(text = "🍽 4 порции", bgColor = Color(0xFFE1F5FE))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Классический итальянский рецепт с беконом, сыром и яйцом...",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onOpenRecipe,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Смотреть рецепт",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BackCard(onBack: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color(0xFFFF7043)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🍝", fontSize = 36.sp)
                Text(
                    text = "Рецепт приготовления",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ингредиенты:",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "• Спагетти — 250 г\n• Бекон / панчетта — 120 г\n• Яичные желтки — 3 шт.\n• Сыр пармезан — 50 г\n• Чеснок, черный перец",
                fontSize = 13.sp,
                color = Color(0xFF455A64),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Приготовление:",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "1. Отварить спагетти аль денте.\n2. Обжарить бекон на сковороде.\n3. Смешать желтки с тертым сыром.\n4. Перемешать горячую пасту с соусом.",
                fontSize = 13.sp,
                color = Color(0xFF455A64),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = "← Назад к карточке",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF37474F)
                )
            }
        }
    }
}

@Composable
fun InfoBadge(text: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF37474F),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeCardPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
    ) {
        FlippableRecipeCard()
    }
}
