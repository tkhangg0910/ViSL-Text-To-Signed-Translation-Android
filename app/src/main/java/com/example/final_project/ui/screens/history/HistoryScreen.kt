package com.example.final_project.ui.screens.history
import com.example.final_project.R
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.GlossRole
import com.example.final_project.domain.model.GlossToken
import com.example.final_project.ui.screens.translate.components.GlossCard
import com.example.final_project.ui.screens.translate.components.getDialectColor
import com.example.final_project.ui.theme.Background
import com.example.final_project.ui.theme.Border
import com.example.final_project.ui.theme.BorderLight
import com.example.final_project.ui.theme.DialectCentral
import com.example.final_project.ui.theme.GlossPlaceBG
import com.example.final_project.ui.theme.GlossPlaceBorder
import com.example.final_project.ui.theme.GlossSBG
import com.example.final_project.ui.theme.GlossSBorder
import com.example.final_project.ui.theme.GlossTimeBG
import com.example.final_project.ui.theme.GlossTimeBorder
import com.example.final_project.ui.theme.Primary
import com.example.final_project.ui.theme.StarYellow
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.SurfaceVariant
import com.example.final_project.ui.theme.TextDisabled
import com.example.final_project.ui.theme.TextHint
import com.example.final_project.ui.theme.TextPrimary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource

@Composable
fun HistoryHeader(
    onDeleteAll: () -> Unit = {},
    ){
    Row(
        modifier = Modifier.fillMaxWidth()
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
                "🕑  ",
                fontSize = 23.sp
            )

            Text(
                stringResource(R.string.history_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )
        }
        Row {
            Button(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .defaultMinSize(minWidth = 4.dp, minHeight = 4.dp),
                onClick = {onDeleteAll()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text("🗑️", fontSize = 20.sp)
            }
        }
    }

}
@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(R.string.confirm_delete_title))
        },
        text = {
            Text(stringResource(R.string.confirm_delete_message))
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(stringResource(R.string.confirm_delete))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit = {},
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
//            .padding(12.dp)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(22.dp)
            ),
        placeholder = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = onClearQuery) {
                Icon(Icons.Default.Cancel, contentDescription = null)
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor        = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor      = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor             = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = TextDisabled,
            unfocusedPlaceholderColor = TextDisabled,
        ),
    )
}
@Composable
fun HistoryScreen(
    vm: HistoryViewModel = hiltViewModel(),
    onPlayItem: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsState()
    val event by vm.events.collectAsState()
    val showStarredOnly by vm.showStarredOnly.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    event?.let { e ->
        when (e) {
            is HistoryEvent.NavigateToTranslate -> {
                onPlayItem()
                vm.onEventConsumed()
            }

            is HistoryEvent.ShowMessage -> {
                Toast.makeText(
                    LocalContext.current,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()

                vm.onEventConsumed()
            }
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HistoryHeader(
                onDeleteAll = {
                    showConfirmDialog = true
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    //                    top = .dp,
                    bottom = 16.dp
                )
        ) {
            if (showConfirmDialog) {
                ConfirmDialog(
                    onDismiss = { showConfirmDialog = false },
                    onConfirm = {
                        showConfirmDialog = false
                        vm.onDeleteAll()
                    }
                )
            }
            SearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    vm.onSearchQueryChange(query)
                },
                onClearQuery = {vm.onClearSearch()}
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                when (uiState) {
                    is HistoryUiState.Loading -> {
                        Text(stringResource(R.string.loading))
                    }

                    is HistoryUiState.Loaded -> {
                        val data = uiState as HistoryUiState.Loaded
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                ),

                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.total_count, data.totalCount)
                                , modifier = Modifier.padding(top = 10.dp, start = 12.dp, bottom = 8.dp)
                            )

                            Button(
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .defaultMinSize(minWidth = 4.dp, minHeight = 4.dp),
                                onClick = {
                                    vm.onToggleStarFilter()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    if(showStarredOnly) painterResource(R.drawable.star_filled_24) else painterResource(R.drawable.star_outlined_24),
                                    contentDescription = null,
                                    tint = if(showStarredOnly) StarYellow else Color.Unspecified,
                                    modifier = Modifier.size(16.dp)
                                )
                            }


                        }
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            data.groupedItems.forEach { it1 ->
                                Text(
                                    text = it1.key.uppercase(),
                                    modifier = Modifier.padding(start = 12.dp),
                                    fontSize = 13.sp,
                                    color = TextHint
                                )
                                data.groupedItems[it1.key]?.forEach { grit ->
                                    HistoryCard(
                                        grit.dialect,
                                        grit.isStarred,
                                        grit.glossTokens,
                                        grit.inputText,
                                        onReplay = {
                                            vm.onPlayAgain(grit)
                                        },
                                        onToggleStar = {
                                            vm.onToggleStar(grit)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }

}


@Composable
fun HistoryCard(
    dialect: Dialect = Dialect.NORTH,
    isStarred: Boolean = false,
    glossTokens: List<GlossToken> = emptyList(),
    inputText: String,
    onReplay: () -> Unit = {},
    onToggleStar: () -> Unit = {},
){
    val clipboardManager = LocalClipboardManager.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(22.dp)
            )
            .background(MaterialTheme.colorScheme.surface,RoundedCornerShape(22.dp))
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth()
            , horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(modifier = Modifier.padding(start = 18.dp, top = 14.dp),
                text = inputText,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .padding(end = 18.dp, top = 14.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(getDialectColor(dialect).second)
                    .border(
                        1.dp,
                        getDialectColor(dialect).third,
                        RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "${dialect.icon} ${stringResource(dialect.displayName)}",
                    color = getDialectColor(dialect).first,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp,6.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            glossTokens.forEachIndexed { index, glossToken ->
                GlossCard(
                    glossToken.role,
                    glossToken.label,
                    fontSize = 10.sp,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 2.dp
                    ),
                    borderWidth = 0.dp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,end=16.dp, bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
            Button(
                onClick = { onReplay() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = GlossSBG,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = GlossSBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ){
                Text(
                    text = stringResource(R.string.replay),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W800,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(inputText))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ){
                Text(
                    text = "📋",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W800,
                )
            }
            Button(
                onClick = { onToggleStar() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = if (isStarred) GlossTimeBG else SurfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if(isStarred) GlossTimeBorder  else BorderLight,
                        shape = RoundedCornerShape(16.dp)
                    )
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ){
                Icon(
                    if(isStarred) painterResource(R.drawable.star_filled_24) else painterResource(R.drawable.star_outlined_24),
                    contentDescription = null,
                    tint = if(isStarred) StarYellow else Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}