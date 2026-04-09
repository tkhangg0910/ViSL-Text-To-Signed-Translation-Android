package com.example.final_project.ui.screens.translate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.domain.model.PipelineState
import com.example.final_project.domain.model.PipelineStep
import com.example.final_project.domain.model.StepStatus
import com.example.final_project.ui.theme.StepDone
import com.example.final_project.ui.theme.StepError
import com.example.final_project.ui.theme.StepRunning
import com.example.final_project.ui.theme.StepWaiting
import com.example.final_project.ui.theme.Surface

@Composable
fun PipelineProgressCard(steps: List<PipelineStep>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface,shape = RoundedCornerShape(16.dp))
            .padding(12.dp),
    ) {
        steps.forEach { step ->
            val color = when (step.status) {
                StepStatus.WAITING -> StepWaiting
                StepStatus.RUNNING -> StepRunning
                StepStatus.DONE    -> StepDone
                StepStatus.ERROR   -> StepError
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = "●",
                        color = color,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = step.name,
                        color = color,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                if (step.status == StepStatus.DONE) {
                    Text(
                        "✓",
                        color = StepDone,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }


        }
    }

}