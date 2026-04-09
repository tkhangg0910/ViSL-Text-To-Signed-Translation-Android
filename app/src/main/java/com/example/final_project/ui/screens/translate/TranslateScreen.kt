package com.example.final_project.ui.screens.translate

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.final_project.ui.screens.translate.components.DialectDropdown
import com.example.final_project.ui.screens.translate.components.InputCard
import com.example.final_project.ui.theme.Background
import com.example.final_project.domain.model.Dialect
import com.example.final_project.ui.theme.Primary
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.final_project.domain.model.GlossToken
import com.example.final_project.domain.model.PipelineState
import com.example.final_project.domain.model.quickChips
import com.example.final_project.ui.screens.translate.components.PipelineProgressCard
import com.example.final_project.ui.screens.translate.components.QuickChipRow
import com.example.final_project.ui.theme.DialectSouth
import com.example.final_project.ui.theme.Secondary
import com.example.final_project.ui.theme.Surface
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import com.example.final_project.R
import com.example.final_project.domain.model.GlossRole
import com.example.final_project.ui.screens.settings.SettingsViewModel
import com.example.final_project.ui.screens.translate.components.GlossSection
import com.example.final_project.ui.theme.SurfaceVariant

@Composable
fun TranslateHeader(
    onNavigateToSettings: () -> Unit = {},
    onPaste: (String) -> Unit
)
{
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
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
        Text(
            buildAnnotatedString {
                append("Vi")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append("SL")
                }
            },
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-1).sp
        )

        Row {
            val pasteSuccess =stringResource(R.string.paste_success)
            val clipboardEmpty =stringResource(R.string.clipboard_empty)

            Button(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .defaultMinSize(minWidth = 4.dp, minHeight = 4.dp),
                onClick = {
                    scope.launch {
                        val clipEntry = clipboard.getClipEntry()
                        val text = clipEntry
                            ?.clipData
                            ?.getItemAt(0)
                            ?.text
                        if (!text.isNullOrEmpty()) {
                            onPaste(text.toString())
                            Toast.makeText(context,pasteSuccess , Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, clipboardEmpty, Toast.LENGTH_SHORT).show()
                        }

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text("📋", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .defaultMinSize(minWidth = 4.dp, minHeight = 4.dp),
                onClick = {onNavigateToSettings()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text("⚙️", fontSize = 20.sp)
            }
        }
    }

}

@Composable
fun TranslateScreen(
    vm: TranslateViewModel = hiltViewModel(),
    settingVm : SettingsViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit = {}
    ) {
    var inputText   by remember { mutableStateOf("") }
    var selectedDialect by remember { mutableStateOf(Dialect.NORTH) }
    val state by vm.pipelineState.collectAsState()
    val context = LocalContext.current
    var glossToken by remember { mutableStateOf<List<GlossToken>>(emptyList()) }
    var videoUri   by remember { mutableStateOf<Uri?>(null) }
    val settingUiState by settingVm.settingUiState.collectAsState()

    val replayInputText by vm.replayInputText.collectAsState()
    val replayDialect   by vm.replayDialect.collectAsState()
    LaunchedEffect(Unit) {
        vm.checkPendingReplay()
    }
    LaunchedEffect(replayInputText) {
        replayInputText?.let { text ->
            replayDialect?.let { selectedDialect = it }
            inputText = text

            val replay = vm.consumeReplayFull()
            if (replay?.uriPath != null) {
                videoUri   = replay.uriPath.toUri()
                glossToken = replay.glossTokens
            }
            vm.onReplayConsumed()
        }
    }
    LaunchedEffect(replayDialect) {
        replayDialect?.let {
            selectedDialect = it
        }
    }


    val sdf = remember { SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()) }
    LaunchedEffect(state) {
        when (val s = state) {
            is PipelineState.Loading -> Log.d("Pipeline", "steps: ${s.steps.map { "${it.index}=${it.status}" }}")
            is PipelineState.Success -> {
                val result = (state as? PipelineState.Success)?.result
                glossToken = result?.glossTokens ?: emptyList()
                Log.d("Pipeline", "Gloss tokens: ${result?.glossTokens}")

                Log.d("Pipeline", "✅ Done! videoPath=${s.result.videoPath}")
                s.result.videoPath?.let { path ->
                    if (path.startsWith("content://")) {
                        videoUri = path.toUri()
                        Log.d("Pipeline", "content uri: $path")
                    } else {
                        val file = File(path)
                        if (file.exists()) {
                            videoUri = Uri.fromFile(file)
                            Log.d("Pipeline", "file exists: ${file.length()} bytes")
                        } else {
                            Log.w("Pipeline", "⚠️ video NOT found: $path")
                        }
                    }
                } ?: Log.w("Pipeline", "⚠️ videoPath is null")
            }
            is PipelineState.Error -> Log.e("Pipeline", "❌ ERROR: ${s.message}")
            else -> Unit
        }
    }
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TranslateHeader(
                onPaste = { inputText = it },
                onNavigateToSettings =onNavigateToSettings
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
//                    top = .dp,
                    bottom = 16.dp
                )
        ) {
            DialectDropdown(
                selectedDialect = selectedDialect,
                onDialectSelected = { selectedDialect = it }
            )

            Spacer(Modifier.height(16.dp))
            InputCard(
                inputText,
                onTextChange = { inputText = it },
                deleteText = {inputText=""
                },
                pickExample = {
                    val randomChip = quickChips.random()
                    inputText=randomChip.text
                }
            )
            Spacer(Modifier.height(10.dp))
            QuickChipRow({
                inputText = it
            })
            Spacer(Modifier.height(14.dp))
            val inputEmpty = stringResource(R.string.input_empty)
            Button(
                onClick = {
                    val text = inputText.trim()
                    if (text.isEmpty()) {
                        Log.w("Pipeline","⚠️ Input is empty")
                        Toast.makeText(context, inputEmpty, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    videoUri=null
                    glossToken= emptyList()
                    Log.d("Pipeline",
                        "▶ translate(dialect=${selectedDialect.apiValue}, topK=$5)")
                    Log.d("Pipeline","  text=\"$text\"")
                    vm.translate(text, selectedDialect, 5)
//                vm.fakeTranslate()
                },
                modifier = Modifier.fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                DialectSouth,
                                Secondary
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        ),
                        shape = RoundedCornerShape(24.dp)

                    )
                    .padding(10.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(R.string.translate_button),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W800,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(14.dp))
            val steps = when (val s = state) {
                is PipelineState.Loading -> s.steps
                is PipelineState.Error   -> s.steps
                else -> emptyList()
            }
//        Log.d("Pipeline",steps.toString())
            if (steps.isNotEmpty()) {
                PipelineProgressCard(steps)
            }

            if (videoUri != null) {
                ExoPlayerView(
                    videoUri,
                    modifier = Modifier.fillMaxWidth().height(220.dp)
                )
            }

            if (glossToken.isNotEmpty() && settingUiState.isGlossVisible){
                GlossSection(
                    glossToken
                )
            }

            val result = (state as? PipelineState.Success)?.result
            Log.d("Pipeline", result?.glossTokens.toString())



        }

    }

}
@Composable
private fun ExoPlayerView(uri: Uri?, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val player  = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(uri) {
        if (uri != null) {
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply { this.player = player }
        },
        modifier = modifier.background(Color.Black),
    )
}