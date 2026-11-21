package com.example.moblabs.lab2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moblabs.R
import com.example.moblabs.lab2.components.DotsMenuIcon
import com.example.moblabs.lab2.components.PetListCard
import com.example.moblabs.ui.theme.MichromaFont
import kotlinx.coroutines.launch

@Composable
fun PetfinderHomeScreen(
    onOpenSearch: () -> Unit,
    onOpenDetails: () -> Unit
) {
    val scrollState = rememberScrollState()
    val categoriesScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val heroImages = listOf(
        R.drawable.hero_dog,
        R.drawable.hero_cat,
        R.drawable.hero_dog,
        R.drawable.hero_cat
    )
    val pagerState = rememberPagerState(pageCount = { heroImages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFB))
            .verticalScroll(scrollState)
            .padding(vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onOpenSearch() },
                contentAlignment = Alignment.CenterStart
            ) {
                DotsMenuIcon()
            }

            Text(
                text = "Petfinder",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MichromaFont,
                color = Color(0xFF1E1E1E)
            )

            Image(
                painter = painterResource(id = R.drawable.user_avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = heroImages[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in heroImages.indices) {
                val isSelected = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 8.dp else 5.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFFF27B2B) else Color(0xFFE0E0E0))
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        val categories = listOf(
            Pair(R.drawable.cat_dogs, "Dogs"),
            Pair(R.drawable.cat_cats, "Cats"),
            Pair(R.drawable.cat_parrots, "Parrots"),
            Pair(R.drawable.cat_bunny, "Bunnies"),
            Pair(R.drawable.cat_hamster, "Hamsters")
        )

        var selectedCategory by remember { mutableStateOf("Dogs") }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(categoriesScrollState)
                .padding(horizontal = 20.dp)
        ) {
            categories.forEach { (iconRes, title) ->
                val isSelected = selectedCategory == title
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedCategory = title
                        onOpenSearch()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(104.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(if (isSelected) Color(0xFFFFF4EC) else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontFamily = MichromaFont,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFFF27B2B) else Color(0xFF9E9E9E)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            PetListCard(
                name = "Trey",
                breed = "Labrador Retriever",
                imageRes = R.drawable.dog_labrador,
                bgColor = Color(0xFFF28F38),
                onClick = onOpenDetails
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            PetListCard(
                name = "Lucille\n& Richie",
                breed = "Basset",
                imageRes = R.drawable.dog_basset,
                bgColor = Color(0xFF6B4019),
                onClick = onOpenDetails
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PetfinderHomeScreen(onOpenSearch = {}, onOpenDetails = {})
}
