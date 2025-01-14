package com.haf.experiment.piechartjetpackcompose.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haf.experiment.piechartjetpackcompose.ui.theme.Blue400
import com.haf.experiment.piechartjetpackcompose.ui.theme.Green400
import com.haf.experiment.piechartjetpackcompose.ui.theme.Orange400
import com.haf.experiment.piechartjetpackcompose.ui.theme.Pink40
import com.haf.experiment.piechartjetpackcompose.ui.theme.Yellow400
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PieChartWithText() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        val chartDataList = listOf(
            ChartData(Pink40, 10f),
            ChartData(Orange400, 20f),
            ChartData(Yellow400, 15f),
            ChartData(Green400, 5f),
            ChartData(Blue400, 50f),
        )

        val textMeasurer = rememberTextMeasurer()
        val textMeasureResults = remember(chartDataList) {
            chartDataList.map {
                textMeasurer.measure(
                    text = "%${it.data.toInt()}",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val width = size.width
            val radius = width / 2f
            val strokeWidth = radius * .4f
            val innerRadius = radius - strokeWidth
            val lineStrokeWidth = 3.dp.toPx()

            var startAngle = -90f

            for (index in 0..chartDataList.lastIndex) {

                val chartData = chartDataList[index]
                val sweepAngle = chartData.data.asAngle
                val angleInRadians = (startAngle + sweepAngle / 2).degreeToAngle
                val textMeasureResult = textMeasureResults[index]
                val textSize = textMeasureResult.size

                drawArc(
                    color = chartData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(width - strokeWidth, width - strokeWidth),
                    style = Stroke(strokeWidth)
                )
                rotate(90f+startAngle){
                    drawLine(
                        color = Color.White,
                        start = Offset(radius, strokeWidth),
                        end = Offset(radius, 0f),
                        strokeWidth = lineStrokeWidth
                    )
                }

                val textCenter = textSize.center

                drawText(
                    textLayoutResult = textMeasureResult,
                    color = Color.Gray,
                    topLeft = Offset(
                        -textCenter.x + center.x + (innerRadius + strokeWidth / 2) * cos(
                            angleInRadians
                        ),
                        -textCenter.y + center.y + (innerRadius + strokeWidth / 2) * sin(
                            angleInRadians
                        )
                    )
                )

                startAngle += sweepAngle
            }
        }
    }
}

private val Float.degreeToAngle
    get() = (this * Math.PI / 180f).toFloat()

private val Float.asAngle: Float
    get() = this * 360f / 100f

@Immutable
data class ChartData(val color: Color, val data: Float)