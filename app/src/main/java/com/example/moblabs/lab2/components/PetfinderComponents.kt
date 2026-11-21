package com.example.moblabs.lab2.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moblabs.ui.theme.MichromaFont

@Composable
fun HeartIcon(modifier: Modifier = Modifier, tint: Color = Color(0xFFB0B0B0), size: Dp = 18.dp) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.85f)
            cubicTo(w * 0.1f, h * 0.58f, 0f, h * 0.32f, w * 0.25f, h * 0.12f)
            cubicTo(w * 0.42f, 0f, w * 0.5f, h * 0.22f, w * 0.5f, h * 0.22f)
            cubicTo(w * 0.5f, h * 0.22f, w * 0.58f, 0f, w * 0.75f, h * 0.12f)
            cubicTo(w, h * 0.32f, w * 0.9f, h * 0.58f, w * 0.5f, h * 0.85f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

@Composable
fun PetListCard(
    name: String,
    breed: String,
    imageRes: Int,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(122.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(96.dp)
                    .clip(RoundedCornerShape(26.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 8.dp, top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MichromaFont,
                    color = Color.White,
                    lineHeight = 22.sp
                )

                Text(
                    text = breed,
                    fontSize = 12.sp,
                    fontFamily = MichromaFont,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            HeartIcon(
                tint = Color(0xFFC0C0C0),
                size = 18.dp,
                modifier = Modifier
                    .padding(top = 20.dp, end = 20.dp)
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun DotsMenuIcon(modifier: Modifier = Modifier, tint: Color = Color(0xFFF27B2B)) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(tint))
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(tint))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(tint))
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(tint))
        }
    }
}

@Composable
fun MapPin(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    color: Color = Color(0xFFF28522),
    alpha: Float = 1.0f,
    hasShadow: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val height = size * 1.36f
    val totalHeight = if (hasShadow) height + 6.dp else height

    Box(
        modifier = modifier
            .size(width = size, height = totalHeight)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.toPx()
            val h = height.toPx()
            val r = w / 2f
            val cx = w / 2f
            val cy = r

            if (hasShadow) {
                drawOval(
                    color = Color.Black.copy(alpha = 0.28f * alpha),
                    topLeft = Offset(cx - r * 0.7f, h - 1.5.dp.toPx()),
                    size = Size(r * 1.4f, 5.dp.toPx())
                )
            }

            val d = (h - cy).coerceAtLeast(r + 1f)
            val sinTheta = (r / d).coerceIn(0f, 1f)
            val cosTheta = kotlin.math.sqrt(1f - sinTheta * sinTheta)
            val tanAngleDeg = (kotlin.math.asin(sinTheta.toDouble()) * 180.0 / Math.PI).toFloat()

            val p1x = cx - r * cosTheta
            val p1y = cy + r * sinTheta

            val path = Path().apply {
                fillType = PathFillType.EvenOdd
                moveTo(cx, h)
                lineTo(p1x, p1y)
                arcTo(
                    rect = Rect(cx - r, cy - r, cx + r, cy + r),
                    startAngleDegrees = 180f - tanAngleDeg,
                    sweepAngleDegrees = 180f + 2f * tanAngleDeg,
                    forceMoveTo = false
                )
                lineTo(cx, h)
                close()

                val holeRadius = r * 0.36f
                addOval(
                    Rect(
                        cx - holeRadius,
                        cy - holeRadius,
                        cx + holeRadius,
                        cy + holeRadius
                    )
                )
            }

            drawPath(
                path = path,
                color = color.copy(alpha = alpha)
            )
        }
    }
}

@Composable
fun SpecItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontFamily = MichromaFont, color = Color(0xFFFFD180))
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = value, fontSize = 14.sp, fontFamily = MichromaFont, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun RatingBarItem(
    title: String,
    score: Int,
    maxScore: Int = 5,
    onScoreChange: ((Int) -> Unit)? = null
) {
    Column {
        Text(text = title, fontSize = 12.sp, fontFamily = MichromaFont, color = Color(0xFF9E9E9E))
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 1..maxScore) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (i <= score) Color(0xFFF27B2B) else Color(0xFFEBEBEB))
                        .then(
                            if (onScoreChange != null) {
                                Modifier.clickable { onScoreChange(i) }
                            } else Modifier
                        )
                )
            }
        }
    }
}
