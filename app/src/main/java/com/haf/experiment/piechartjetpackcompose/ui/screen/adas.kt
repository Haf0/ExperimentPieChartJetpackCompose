package com.haf.experiment.piechartjetpackcompose.ui.screen

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


data class PieChartData(
    val color: Color= getRandomColor(),
    val value: Int,
    val description: String,
    val isTapped: Boolean = false
)

internal fun getRandomColor(): Color {
    return Color(
        red = (0..255).random(),
        blue =  (0..255).random(),
        green =  (0..255).random()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPieChart(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    cModifier: Modifier = Modifier,
    innerRadius: Float = 120f,
    dataPoints: List<PieChartData>,
    onSliceClick: ((PieChartData) -> Unit),
) {
    var pieCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var inputList by remember {
        mutableStateOf(dataPoints)
    }
    var isCenterTapped by remember {
        mutableStateOf(false)
    }



    val textMeasurer = rememberTextMeasurer()
    val gapDegrees = 2f
    val numberOfGaps = dataPoints.size
    val remainingDegrees = 360f - (gapDegrees * numberOfGaps)


    val totalValue = dataPoints.sumOf {
        it.value
    }
    val anglePerValue = remainingDegrees / totalValue
    Canvas(
        modifier = cModifier
            .size(200.dp)
            .pointerInput(true) {
                detectTapGestures(
                    onTap = { offset ->  //point where clicked
                        val angle = Math.toDegrees(
                            atan2(
                                offset.y - pieCenter.y,
                                offset.x - pieCenter.x
                            ).toDouble()
                        )
                        val tapAngleInDegrees = if (angle < 0) angle + 360 else angle


                        val centerClicked = if (tapAngleInDegrees < 90) {
                            offset.x < pieCenter.x + innerRadius && offset.y < pieCenter.y + innerRadius
                        } else if (tapAngleInDegrees < 180) {
                            offset.x > pieCenter.x - innerRadius && offset.y < pieCenter.y + innerRadius
                        } else if (tapAngleInDegrees < 270) {
                            offset.x > pieCenter.x - innerRadius && offset.y > pieCenter.y - innerRadius
                        } else {
                            offset.x < pieCenter.x + innerRadius && offset.y > pieCenter.y - innerRadius
                        }

                        if (centerClicked) {
                            inputList = inputList.map {
                                it.copy(isTapped = !isCenterTapped)
                            }
                            isCenterTapped = !isCenterTapped
                        } else {
                            var currAngle = 0f
                            inputList.forEach { pieChartInput ->

                                currAngle += pieChartInput.value * anglePerValue
                                if (tapAngleInDegrees < currAngle) {
                                    val description = pieChartInput.description
                                    inputList = inputList.map {
                                        if (description == it.description) {
                                            onSliceClick(it)
                                            it.copy(isTapped = !it.isTapped)
                                        } else {
                                            it.copy(isTapped = false)
                                        }
                                    }
                                    return@detectTapGestures
                                }
                            }
                        }
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height

        val radius = width / 2

        pieCenter = Offset(x = width / 2f, y = height / 2f)

        var currentStartAngle = 0f

        val donutStyle = Stroke(
            width = 100f,
            cap = StrokeCap.Round
        )

        inputList.forEach { pieChartInput ->
            val scale = if (pieChartInput.isTapped) 0.78f else 0.75f
            val angleToDraw = pieChartInput.value * anglePerValue

            scale(scale) {
                drawArc(
                    color = pieChartInput.color,
                    startAngle = currentStartAngle, sweepAngle = angleToDraw, useCenter = true,
                    size = Size(
                        width = radius * 2f,
                        height = radius * 2f
                    ),
                    topLeft = Offset(
                        (width - radius * 2f) / 2f,
                        (height - radius * 2f) / 2f
                    ),
                    //style = donutStyle
                )
                currentStartAngle += angleToDraw + gapDegrees
            }

            //percentage value of each data point
            val percentage = (pieChartInput.value / totalValue.toFloat() * 100).toInt()

            //draw text inside the slice to show the percentage of data
            drawContext.canvas.nativeCanvas.apply {
                //only show data if > 5%
                if (percentage > 5) {
                    val midAngle = currentStartAngle - gapDegrees - angleToDraw / 2f
                    val midOffSet = Offset(
                        x = (cos(Math.toRadians(midAngle.toDouble())) * radius + pieCenter.x).toFloat(),
                        y = (sin(Math.toRadians(midAngle.toDouble())) * radius + pieCenter.y).toFloat()
                    )

                    val xOffset = (midOffSet.x + pieCenter.x) / 2
                    val yOffset = (midOffSet.y + pieCenter.y) / 2

                    val centerOfSlice = Offset(xOffset, yOffset)

                    //measure text if it have any style like font, size, letter spacing
                    val textLayoutResult = textMeasurer.measure(
                        text = "$percentage %"
                    )
                    val textWidth = textLayoutResult.size.width
                    val textHeight = textLayoutResult.size.height

                    drawText(
                        textLayoutResult, color = Color.Black,
                        topLeft = Offset(
                            centerOfSlice.x - textWidth / 2,
                            centerOfSlice.y - textHeight / 2
                        )
                    )

                }
            }

            //angle for the text outside the slice
            var rotateAngle = currentStartAngle - gapDegrees - angleToDraw / 2f - 90f
            //how much distance from center you want to draw text outside the slice
            var radiusFactor = .9f
            if (rotateAngle > 90f) {
                rotateAngle = (rotateAngle + 180).mod(360f)
                //above 90 make text angle to negative
                radiusFactor = -.9f
            }

            //show text outside the pie when tapped on a slice
            if (pieChartInput.isTapped) {
                rotate(rotateAngle) {
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${pieChartInput.description}: ${pieChartInput.value}",
                            pieCenter.x,
                            pieCenter.y + radius.times(radiusFactor),
                            Paint().apply {
                                textSize = 16.sp.toPx()
                                textAlign = Paint.Align.CENTER
                                color = Color.Black.toArgb()
                            }
                        )
                    }
                }
            }

            //circle at the center of pie
            drawCircle(
                center = pieCenter,
                color = Color.White,
                radius = innerRadius
            )
        }

    }
}