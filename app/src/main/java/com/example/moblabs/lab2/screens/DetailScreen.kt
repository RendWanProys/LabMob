package com.example.moblabs.lab2.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moblabs.R
import com.example.moblabs.lab2.components.HeartIcon
import com.example.moblabs.lab2.components.RatingBarItem
import com.example.moblabs.lab2.components.SpecItem
import com.example.moblabs.ui.theme.MichromaFont
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import org.json.JSONArray
import org.json.JSONObject

data class PetItem(
    val id: String,
    val name: String,
    val breed: String,
    val sex: String,
    val age: String,
    val weight: String,
    val color: String,
    val playfulness: Int,
    val activeLevel: Int,
    val friendliness: Int,
    val imageFileName: String = "",
    val defaultImageRes: Int? = null,
    val isLiked: Boolean = false
)

val defaultBombay = PetItem(
    id = "pet_bombay_default",
    name = "Bombay",
    breed = "British Breed",
    sex = "Mail",
    age = "11 Months",
    weight = "4-5 kg",
    color = "Black",
    playfulness = 5,
    activeLevel = 4,
    friendliness = 3,
    defaultImageRes = R.drawable.cat_bombay,
    imageFileName = "",
    isLiked = false
)

fun savePetsListToPrefs(context: Context, pets: List<PetItem>) {
    try {
        val array = JSONArray()
        for (pet in pets) {
            val obj = JSONObject()
            obj.put("id", pet.id)
            obj.put("name", pet.name)
            obj.put("breed", pet.breed)
            obj.put("sex", pet.sex)
            obj.put("age", pet.age)
            obj.put("weight", pet.weight)
            obj.put("color", pet.color)
            obj.put("playfulness", pet.playfulness)
            obj.put("activeLevel", pet.activeLevel)
            obj.put("friendliness", pet.friendliness)
            obj.put("imageFileName", pet.imageFileName)
            obj.put("isLiked", pet.isLiked)
            if (pet.defaultImageRes != null) {
                obj.put("defaultImageRes", pet.defaultImageRes)
            }
            array.put(obj)
        }
        context.getSharedPreferences("pets_feed_v2", Context.MODE_PRIVATE)
            .edit()
            .putString("pets_json", array.toString())
            .apply()
    } catch (e: Exception) {
    }
}

fun loadPetsListFromPrefs(context: Context): List<PetItem> {
    val prefs = context.getSharedPreferences("pets_feed_v2", Context.MODE_PRIVATE)
    val jsonStr = prefs.getString("pets_json", null) ?: return listOf(defaultBombay)
    return try {
        val array = JSONArray(jsonStr)
        val list = mutableListOf<PetItem>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                PetItem(
                    id = obj.optString("id", UUID.randomUUID().toString()),
                    name = obj.optString("name", "Pet"),
                    breed = obj.optString("breed", "Breed"),
                    sex = obj.optString("sex", "Mail"),
                    age = obj.optString("age", "11 Months"),
                    weight = obj.optString("weight", "4-5 kg"),
                    color = obj.optString("color", "Black"),
                    playfulness = obj.optInt("playfulness", 5),
                    activeLevel = obj.optInt("activeLevel", 4),
                    friendliness = obj.optInt("friendliness", 3),
                    imageFileName = obj.optString("imageFileName", ""),
                    defaultImageRes = if (obj.has("defaultImageRes")) obj.getInt("defaultImageRes") else null,
                    isLiked = obj.optBoolean("isLiked", false)
                )
            )
        }
        if (list.isEmpty()) listOf(defaultBombay) else list
    } catch (e: Exception) {
        listOf(defaultBombay)
    }
}

fun loadSafeBitmap(file: File): ImageBitmap? {
    return try {
        if (!file.exists() || file.length() == 0L) return null
        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, boundsOptions)
        var sampleSize = 1
        val maxDim = maxOf(boundsOptions.outWidth, boundsOptions.outHeight)
        if (maxDim <= 0) return null
        while (maxDim / sampleSize > 1000) {
            sampleSize *= 2
        }
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        BitmapFactory.decodeFile(file.absolutePath, decodeOptions)?.asImageBitmap()
    } catch (t: Throwable) {
        null
    }
}

fun saveSafePhoto(context: Context, uri: Uri, destFileName: String): String? {
    return try {
        val destFile = File(context.filesDir, destFileName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }
        if (destFile.exists() && destFile.length() > 0) destFileName else null
    } catch (t: Throwable) {
        null
    }
}

@Composable
fun PetDetailScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var petsList by remember { mutableStateOf(loadPetsListFromPrefs(context)) }

    var showAddPetDialog by remember { mutableStateOf(false) }
    var addName by remember { mutableStateOf("") }
    var addBreed by remember { mutableStateOf("") }
    var addSex by remember { mutableStateOf("Mail") }
    var addAge by remember { mutableStateOf("11 Months") }
    var addWeight by remember { mutableStateOf("4-5 kg") }
    var addColor by remember { mutableStateOf("Black") }
    var addPlayfulness by remember { mutableIntStateOf(5) }
    var addActiveLevel by remember { mutableIntStateOf(4) }
    var addFriendliness by remember { mutableIntStateOf(3) }
    var addImageFileName by remember { mutableStateOf("") }

    var showEditPetDialog by remember { mutableStateOf(false) }
    var editingPetId by remember { mutableStateOf("") }
    var editName by remember { mutableStateOf("") }
    var editBreed by remember { mutableStateOf("") }
    var editSex by remember { mutableStateOf("") }
    var editAge by remember { mutableStateOf("") }
    var editWeight by remember { mutableStateOf("") }
    var editColor by remember { mutableStateOf("") }
    var editPlayfulness by remember { mutableIntStateOf(5) }
    var editActiveLevel by remember { mutableIntStateOf(4) }
    var editFriendliness by remember { mutableIntStateOf(3) }
    var editImageFileName by remember { mutableStateOf("") }

    var pendingPhotoSetter by remember { mutableStateOf<((String) -> Unit)?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = "pet_photo_${System.currentTimeMillis()}.jpg"
            val saved = saveSafePhoto(context, uri, fileName)
            if (saved != null) {
                pendingPhotoSetter?.invoke(saved)
            }
        }
    }

    val addBitmap = remember(addImageFileName) {
        if (addImageFileName.isNotEmpty()) loadSafeBitmap(File(context.filesDir, addImageFileName)) else null
    }

    val editBitmap = remember(editImageFileName) {
        if (editImageFileName.isNotEmpty()) loadSafeBitmap(File(context.filesDir, editImageFileName)) else null
    }

    val feedScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(feedScrollState)
    ) {
        petsList.forEachIndexed { index, pet ->
            PetProfileCard(
                pet = pet,
                isFirst = index == 0,
                onBack = onBack,
                onAddBreeds = {
                    addName = ""
                    addBreed = ""
                    addSex = "Mail"
                    addAge = "1 Year"
                    addWeight = "4 kg"
                    addColor = "Golden"
                    addPlayfulness = 5
                    addActiveLevel = 4
                    addFriendliness = 4
                    addImageFileName = ""
                    showAddPetDialog = true
                },
                onEdit = {
                    editingPetId = pet.id
                    editName = pet.name
                    editBreed = pet.breed
                    editSex = pet.sex
                    editAge = pet.age
                    editWeight = pet.weight
                    editColor = pet.color
                    editPlayfulness = pet.playfulness
                    editActiveLevel = pet.activeLevel
                    editFriendliness = pet.friendliness
                    editImageFileName = pet.imageFileName
                    showEditPetDialog = true
                },
                onToggleLike = {
                    val updated = petsList.map {
                        if (it.id == pet.id) it.copy(isLiked = !it.isLiked) else it
                    }
                    petsList = updated
                    savePetsListToPrefs(context, updated)
                }
            )

            if (index < petsList.size - 1) {
                Spacer(modifier = Modifier.height(28.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = Color(0xFFF0E5DC),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                addName = ""
                addBreed = ""
                addSex = "Mail"
                addAge = "1 Year"
                addWeight = "4 kg"
                addColor = "Golden"
                addPlayfulness = 5
                addActiveLevel = 4
                addFriendliness = 4
                addImageFileName = ""
                showAddPetDialog = true
            },
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF27B2B)),
            border = BorderStroke(1.5.dp, Color(0xFFF27B2B)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = "+ ДОБАВИТЬ ЕЩЕ ОДНОГО ПИТОМЦА",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MichromaFont,
                color = Color(0xFFF27B2B)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }

    if (showAddPetDialog) {
        val addDialogScrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showAddPetDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val newPet = PetItem(
                            id = UUID.randomUUID().toString(),
                            name = addName.ifBlank { "Buddy" },
                            breed = addBreed.ifBlank { "Golden Retriever" },
                            sex = addSex.ifBlank { "Mail" },
                            age = addAge.ifBlank { "1 Year" },
                            weight = addWeight.ifBlank { "4 kg" },
                            color = addColor.ifBlank { "Golden" },
                            playfulness = addPlayfulness,
                            activeLevel = addActiveLevel,
                            friendliness = addFriendliness,
                            imageFileName = addImageFileName,
                            defaultImageRes = if (addImageFileName.isEmpty()) R.drawable.dog_labrador else null,
                            isLiked = false
                        )
                        val updated = petsList + newPet
                        petsList = updated
                        savePetsListToPrefs(context, updated)
                        showAddPetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF27B2B)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Добавить", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = MichromaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPetDialog = false }) {
                    Text("Отмена", color = Color.Gray, fontFamily = MichromaFont)
                }
            },
            title = {
                Text("Новый питомец", fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MichromaFont)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp)
                        .verticalScroll(addDialogScrollState)
                ) {
                    OutlinedButton(
                        onClick = {
                            pendingPhotoSetter = { addImageFileName = it }
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📷 Выбрать фото с устройства", fontFamily = MichromaFont)
                    }

                    if (addBitmap != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            bitmap = addBitmap,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = addName,
                        onValueChange = { addName = it },
                        label = { Text("Кличка", fontFamily = MichromaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = addBreed,
                        onValueChange = { addBreed = it },
                        label = { Text("Порода", fontFamily = MichromaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = addSex,
                            onValueChange = { addSex = it },
                            label = { Text("Пол", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = addAge,
                            onValueChange = { addAge = it },
                            label = { Text("Возраст", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = addWeight,
                            onValueChange = { addWeight = it },
                            label = { Text("Вес", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = addColor,
                            onValueChange = { addColor = it },
                            label = { Text("Окрас", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Параметры:", fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = MichromaFont)
                    Spacer(modifier = Modifier.height(8.dp))

                    RatingBarItem(
                        title = "Playfulness",
                        score = addPlayfulness,
                        onScoreChange = { addPlayfulness = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingBarItem(
                        title = "Active level",
                        score = addActiveLevel,
                        onScoreChange = { addActiveLevel = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingBarItem(
                        title = "Friendliness to other pets",
                        score = addFriendliness,
                        onScoreChange = { addFriendliness = it }
                    )
                }
            }
        )
    }

    if (showEditPetDialog) {
        val editDialogScrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showEditPetDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = petsList.map {
                            if (it.id == editingPetId) {
                                it.copy(
                                    name = editName.ifBlank { it.name },
                                    breed = editBreed.ifBlank { it.breed },
                                    sex = editSex.ifBlank { it.sex },
                                    age = editAge.ifBlank { it.age },
                                    weight = editWeight.ifBlank { it.weight },
                                    color = editColor.ifBlank { it.color },
                                    playfulness = editPlayfulness,
                                    activeLevel = editActiveLevel,
                                    friendliness = editFriendliness,
                                    imageFileName = if (editImageFileName.isNotEmpty()) editImageFileName else it.imageFileName
                                )
                            } else it
                        }
                        petsList = updated
                        savePetsListToPrefs(context, updated)
                        showEditPetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF27B2B)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Сохранить", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = MichromaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditPetDialog = false }) {
                    Text("Отмена", color = Color.Gray, fontFamily = MichromaFont)
                }
            },
            title = {
                Text("Данные питомца", fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MichromaFont)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp)
                        .verticalScroll(editDialogScrollState)
                ) {
                    OutlinedButton(
                        onClick = {
                            pendingPhotoSetter = { editImageFileName = it }
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📷 Выбрать фото с устройства", fontFamily = MichromaFont)
                    }

                    if (editBitmap != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            bitmap = editBitmap,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Кличка", fontFamily = MichromaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editBreed,
                        onValueChange = { editBreed = it },
                        label = { Text("Порода", fontFamily = MichromaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editSex,
                            onValueChange = { editSex = it },
                            label = { Text("Пол", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editAge,
                            onValueChange = { editAge = it },
                            label = { Text("Возраст", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editWeight,
                            onValueChange = { editWeight = it },
                            label = { Text("Вес", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editColor,
                            onValueChange = { editColor = it },
                            label = { Text("Окрас", fontFamily = MichromaFont) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Параметры:", fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = MichromaFont)
                    Spacer(modifier = Modifier.height(8.dp))

                    RatingBarItem(
                        title = "Playfulness",
                        score = editPlayfulness,
                        onScoreChange = { editPlayfulness = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingBarItem(
                        title = "Active level",
                        score = editActiveLevel,
                        onScoreChange = { editActiveLevel = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingBarItem(
                        title = "Friendliness to other pets",
                        score = editFriendliness,
                        onScoreChange = { editFriendliness = it }
                    )
                }
            }
        )
    }
}

@Composable
fun PetProfileCard(
    pet: PetItem,
    isFirst: Boolean,
    onBack: () -> Unit,
    onAddBreeds: () -> Unit,
    onEdit: () -> Unit,
    onToggleLike: () -> Unit
) {
    val context = LocalContext.current
    val bitmap = remember(pet.imageFileName) {
        if (pet.imageFileName.isNotEmpty()) {
            loadSafeBitmap(File(context.filesDir, pet.imageFileName))
        } else null
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = pet.defaultImageRes ?: R.drawable.cat_bombay),
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isFirst) {
                    IconButton(onClick = onBack) {
                        Text(text = "←", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Text(
                    text = pet.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MichromaFont,
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .clickable { onAddBreeds() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "BREEDS",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = MichromaFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF5A3110)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .offset(y = (-30).dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = pet.breed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MichromaFont,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    SpecItem(label = "Sex", value = pet.sex, modifier = Modifier.weight(1f))
                    SpecItem(label = "Age", value = pet.age, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    SpecItem(label = "Weigth", value = pet.weight, modifier = Modifier.weight(1f))
                    SpecItem(label = "Color", value = pet.color, modifier = Modifier.weight(1f))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .offset(y = (-14).dp)
        ) {
            Text(
                text = "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MichromaFont,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RatingBarItem(
                title = "Playfulness",
                score = pet.playfulness,
                onScoreChange = null
            )

            Spacer(modifier = Modifier.height(14.dp))

            RatingBarItem(
                title = "Active level",
                score = pet.activeLevel,
                onScoreChange = null
            )

            Spacer(modifier = Modifier.height(14.dp))

            RatingBarItem(
                title = "Friendliness to other pets",
                score = pet.friendliness,
                onScoreChange = null
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF27B2B)),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(
                        text = "ADOPTION",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MichromaFont,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Surface(
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier
                        .size(52.dp)
                        .clickable { onToggleLike() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        HeartIcon(
                            tint = if (pet.isLiked) Color(0xFFF27B2B) else Color(0xFFC0C0C0),
                            size = 22.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    PetDetailScreen(onBack = {})
}