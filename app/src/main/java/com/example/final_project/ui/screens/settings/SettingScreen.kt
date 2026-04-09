package com.example.final_project.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.final_project.ui.screens.history.ConfirmDialog
import com.example.final_project.ui.screens.history.HistoryViewModel
import com.example.final_project.ui.theme.Background
import com.example.final_project.ui.theme.Border
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.TextHint
import com.example.final_project.R
@Composable
fun SettingHeader(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .statusBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 12.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "⚙️  ",
                fontSize = 23.sp
            )
            Text(
                stringResource(R.string.settings_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )

        }
    }
}

@Composable
fun SettingsScreen(
    histVm: HistoryViewModel = hiltViewModel(),
    settingVm : SettingsViewModel = hiltViewModel()

) {
    val languages = listOf(
        Language("vi", stringResource(R.string.lang_vi),
            stringResource(R.string.lang_vi_sub), "🇻🇳"),
        Language("en", stringResource(R.string.lang_en),
            stringResource(R.string.lang_en_sub), "🇬🇧")
    )

    var showConfirmDialog by remember { mutableStateOf(false) }
    val totalCount by histVm.count.collectAsState()
    val uiState by settingVm.settingUiState.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val currentLang = uiState.languageCode
    val currentLanguageName = languages.find { it.code == currentLang }?.name ?: "Unknown"

//    LaunchedEffect(uiState) {
//        Log.d("SettingsScreen", "isDarkMode: ${uiState.isDarkMode}")
//        Log.d("SettingsScreen", "currentLang: $currentLang")
//        Log.d("SettingsScreen", "isGlossVisible: ${uiState.isGlossVisible}\n")
//    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SettingHeader(
            )
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)

        )
        {
            if (showConfirmDialog) {
                ConfirmDialog(
                    onDismiss = { showConfirmDialog = false },
                    onConfirm = {
                        showConfirmDialog = false
                        histVm.onDeleteAll()
                    }
                )
            }
            if (showSheet) {
                LanguageBottomSheet(
                    currentLang = currentLang,
                    onSelect = {
                        settingVm.changeLanguage(it)
                    },
                    onClose = {showSheet = false}

                )
            }

            Column {

                Text(
                    text = stringResource(R.string.section_ui).uppercase(),
                    modifier = Modifier.padding(bottom = 6.dp),
                    fontSize = 14.sp,
                    color = TextHint
                )

                // UI Setting
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(22.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Nightlight,
                            tint = Color(0xFFF4D27A),
                            contentDescription = null
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.dark_mode),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                if (uiState.isDarkMode) stringResource(R.string.dark_mode_on)
                                else stringResource(R.string.dark_mode_off),
                                fontSize = 12.sp,
                                color = TextHint
                            )
                        }

                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = {
                                settingVm.changeDarkMode(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.White
                            )
                        )
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 12.dp)

                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                onClick = {
                                    showSheet = true
                                }
                            )
                            .padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                stringResource(R.string.choose_language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                currentLanguageName,
                                fontSize = 12.sp,
                                color = TextHint
                            )
                        }
                    }
                }
            }

            Column {

                Text(
                    text = stringResource(R.string.section_translation).uppercase(),
                    modifier = Modifier.padding(bottom = 6.dp),
                    fontSize = 14.sp,
                    color = TextHint
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(22.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Abc,
                            tint = Color(0xFF87CEEB),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                stringResource(R.string.show_gloss),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                if (uiState.isGlossVisible)
                                    stringResource(R.string.gloss_on)
                                else stringResource(R.string.gloss_off),
                                fontSize = 12.sp,
                                color = TextHint
                            )
                        }

                        Switch(
                            checked = uiState.isGlossVisible,
                            onCheckedChange = {
                                settingVm.changeGlossVisible(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.White
                            )
                        )
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(R.string.section_data).uppercase(),
                    modifier = Modifier.padding(bottom = 6.dp),
                    fontSize = 14.sp,
                    color = TextHint
                )

                // UI Setting
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(22.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🗑️",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    onClick = {
                                        showConfirmDialog = true
                                    }
                                )
                        ) {
                            Text(
                                stringResource(R.string.delete_history),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                stringResource(R.string.translated_count, totalCount),
                                fontSize = 12.sp,
                                color = TextHint
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 12.dp)

                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text("ℹ️",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                stringResource(R.string.about),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                stringResource(R.string.app_version),
                                fontSize = 12.sp,
                                color = TextHint
                            )
                        }
                    }
                }

            }

        }
    }
}


data class Language(
    val code: String,
    val name: String,
    val sub: String,
    val flag: String
)
@Composable
fun LanguageItem(
    language: Language,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) Color(0xFF00C896) else Color.Transparent
    val bgColor = if (selected) Color(0x1100C896) else MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = language.flag,
            fontSize = 24.sp,
            modifier = Modifier.width(40.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = language.name,
                color = if (selected) Color(0xFF00C896) else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = language.sub,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color(0xFF00C896), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    currentLang: String,
    onSelect: (String) -> Unit,
    onClose: () -> Unit
){
    val languages = listOf(
        Language("vi", stringResource(R.string.lang_vi),
            stringResource(R.string.lang_vi_sub), "🇻🇳"),
        Language("en", stringResource(R.string.lang_en),
            stringResource(R.string.lang_en_sub), "🇬🇧")
    )

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = { onClose() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Title
            Text(
                text = "🌐 ${stringResource(R.string.choose_language)}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            languages.forEach { lang ->
                LanguageItem(
                    language = lang,
                    selected = lang.code == currentLang,
                    onClick = {
                        onSelect(lang.code)
                        onClose()
                    }
                )
            }

//            Spacer(modifier = Modifier.height(16.dp))

//            Button(
//                onClick = { onClose() },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("Xong ✓")
//            }
        }

    }
}
