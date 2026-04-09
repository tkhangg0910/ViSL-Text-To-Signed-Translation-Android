package com.example.final_project.ui.screens.debug

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.final_project.domain.model.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private val BgDark    = Color(0xFF1A1A2E)
private val BgCard    = Color(0xFF16213E)
private val BgLogBox  = Color(0xFF0D0D1A)
private val TextMain  = Color(0xFFE0E0FF)
private val TextLabel = Color(0xFF9090CC)
private val TextMono  = Color(0xFFC0C0FF)
private val TextLog   = Color(0xFF44FF88)
private val AccentBtn = Color(0xFF7B8CDE)

@Composable
fun TranslationDebugScreen(
    vm: TranslationDebugViewModel = hiltViewModel(),
) {
    val state by vm.pipelineState.collectAsState()

    // Local UI state
    var inputText   by remember { mutableStateOf("") }
    var selectedIdx by remember { mutableIntStateOf(0) }
    var topK        by remember { mutableIntStateOf(5) }
    var debugLog    by remember { mutableStateOf("") }
    var videoUri    by remember { mutableStateOf<Uri?>(null) }

    // Timestamp helper
    val sdf = remember { SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()) }
    fun log(msg: String) {
        debugLog += "[${sdf.format(Date())}] $msg\n"
    }

    // React to pipeline state changes
    LaunchedEffect(state) {
        when (val s = state) {
            is PipelineState.Loading -> log("  steps: ${s.steps.map { "${it.index}=${it.status}" }}")
            is PipelineState.Success -> {
                log("✅ Done! videoPath=${s.result.videoPath}")
                s.result.videoPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        videoUri = Uri.fromFile(file)
                        log("  video exists: ${file.length()} bytes")
                    } else {
                        log("⚠️ video NOT found: $path")
                    }
                } ?: log("⚠️ videoPath is null")
            }
            is PipelineState.Error -> log("❌ ERROR: ${s.message}")
            else -> Unit
        }
    }

    val isLoading = state is PipelineState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // ── Header ───────────────────────────────────────
        Text("🧪 ViSL Debug Prototype",
            color = TextMain, fontSize = 20.sp,
            fontFamily = FontFamily.Monospace)

        Spacer(Modifier.height(16.dp))

        // ── Input ────────────────────────────────────────
        DebugLabel("Input Text")
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
            placeholder = { Text("Nhập câu cần dịch...", color = Color(0xFF555577)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = BgCard,
                unfocusedContainerColor = BgCard,
                focusedTextColor        = TextMain,
                unfocusedTextColor      = TextMain,
            ),
        )

        Spacer(Modifier.height(12.dp))

        // ── Dialect ──────────────────────────────────────
        DebugLabel("Dialect")
        val dialects = Dialect.entries
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            dialects.forEachIndexed { idx, dialect ->
                FilterChip(
                    selected = selectedIdx == idx,
                    onClick  = { selectedIdx = idx },
                    label    = { Text("${dialect.icon} ${dialect.apiValue}",
                        color = TextMain, fontSize = 12.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentBtn,
                    ),
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ── Top-K ────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Top-K: $topK", color = TextLabel, fontSize = 12.sp,
                modifier = Modifier.width(80.dp))
            Slider(
                value    = topK.toFloat(),
                onValueChange = { topK = it.toInt() },
                valueRange = 1f..10f,
                steps    = 8,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Translate Button ─────────────────────────────
        Button(
            onClick = {
                val text = inputText.trim()
                if (text.isEmpty()) { log("⚠️ Input is empty"); return@Button }
                log("▶ translate(dialect=${dialects[selectedIdx].apiValue}, topK=$topK)")
                log("  text=\"$text\"")
                vm.translate(text, dialects[selectedIdx], topK)
            },
            enabled  = !isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = AccentBtn),
        ) {
            Text(if (isLoading) "Translating…" else "▶  Translate",
                color = Color(0xFF1A1A2E))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp),
            color = Color(0xFF2A2A4A))

        // ── Pipeline Steps ───────────────────────────────
        DebugLabel("Pipeline Steps")
        val steps = when (val s = state) {
            is PipelineState.Loading -> s.steps
            is PipelineState.Error   -> s.steps
            else -> emptyList()
        }
        PipelineStepsPanel(steps)

        // ── Status ───────────────────────────────────────
        val statusText = when (val s = state) {
            is PipelineState.Idle    -> "Status: Idle"
            is PipelineState.Loading -> "Status: Loading…"
            is PipelineState.Success -> "Status: ✅ Success (id=${s.result.id})"
            is PipelineState.Error   -> "Status: ❌ ${s.message}"
        }
        Text(statusText, color = TextLabel, fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp),
            color = Color(0xFF2A2A4A))

        // ── Result ───────────────────────────────────────
        DebugLabel("Result")
        val result = (state as? PipelineState.Success)?.result

        MonoBox(label = "Gloss",
            value = result?.glossTokens
                ?.joinToString("  ") { "[${it.role.label}] ${it.label}" }
                ?: "—")

        Spacer(Modifier.height(6.dp))

        MonoBox(label = "Tokens",
            value = result?.tokens?.joinToString(" | ") ?: "—")

        // ── Video ─────────────────────────────────────────
        Spacer(Modifier.height(12.dp))
        DebugLabel("Video Output")
        ExoPlayerView(uri = videoUri,
            modifier = Modifier.fillMaxWidth().height(220.dp))

        Text("videoPath: ${result?.videoPath ?: "—"}",
            color = Color(0xFF555577), fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(top = 4.dp))

        // ── Debug Log ─────────────────────────────────────
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            DebugLabel("Debug Log", modifier = Modifier.weight(1f))
            TextButton(onClick = { debugLog = "" }) {
                Text("Clear", color = TextLabel, fontSize = 12.sp)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(BgLogBox)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(debugLog, color = TextLog, fontSize = 11.sp,
                fontFamily = FontFamily.Monospace)
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
private fun DebugLabel(text: String, modifier: Modifier = Modifier) {
    Text(text, color = Color(0xFF9090CC), fontSize = 12.sp,
        modifier = modifier.padding(bottom = 4.dp))
}

@Composable
private fun MonoBox(label: String, value: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF16213E))
        .padding(10.dp)
    ) {
        Text("$label: $value", color = Color(0xFFC0C0FF),
            fontSize = 13.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun PipelineStepsPanel(steps: List<PipelineStep>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF16213E))
            .padding(12.dp),
    ) {
        if (steps.isEmpty()) {
            Text("(no active pipeline)", color = Color(0xFF555577), fontSize = 11.sp)
            return@Column
        }
        steps.forEach { step ->
            val (icon, color) = when (step.status) {
                StepStatus.WAITING -> "⏳" to Color(0xFF888899)
                StepStatus.RUNNING -> "🔄" to Color(0xFFFFCC44)
                StepStatus.DONE    -> "✅" to Color(0xFF44CC88)
                StepStatus.ERROR   -> "❌" to Color(0xFFFF5555)
            }
            Text("$icon  [${step.index}] ${step.name}",
                color = color, fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 2.dp))
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