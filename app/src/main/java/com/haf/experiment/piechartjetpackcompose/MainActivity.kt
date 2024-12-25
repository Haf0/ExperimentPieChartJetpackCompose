@file:OptIn(ExperimentalLayoutApi::class)

package com.haf.experiment.piechartjetpackcompose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haf.experiment.piechartjetpackcompose.ui.screen.AnimatedPieChart
import com.haf.experiment.piechartjetpackcompose.ui.screen.PieChart
import com.haf.experiment.piechartjetpackcompose.ui.screen.PieChartData
import com.haf.experiment.piechartjetpackcompose.ui.screen.SimpleAnimatedPieChart
import com.haf.experiment.piechartjetpackcompose.ui.screen.SimplePieChartData
import com.haf.experiment.piechartjetpackcompose.ui.theme.PieChartJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PieChartJetpackComposeTheme {
                Scaffold(modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        name2()
                    }
                }
            }
        }
    }
}

@Composable
fun dunnto(modifier: Modifier = Modifier) {

    val list = listOf(
        PieChartData(
            value = 29,
            description = "Data 1222"
        ),
        PieChartData(
            value = 21,
            description = "Data 2213"
        ),
        PieChartData(
            value = 32,
            description = "Data 31312"
        ),
        PieChartData(
            value = 18,
            description = "Data 412312"
        ),
        PieChartData(
            value = 12,
            description = "Data 1312"
        ),
        PieChartData(
            value = 38,
            description = "Data 61231"
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedPieChart(
            modifier = Modifier.padding(11.dp),
            dataPoints = list,
            onSliceClick = {

            }
        )

        PieChart(
            modifier = Modifier.padding(11.dp),
            dataPoints = list,
            onSliceClick = {

            }
        )
    }


}

@Composable
fun name2(modifier: Modifier = Modifier) {
    val sampleData = listOf(
        SimplePieChartData(
            color = Color(0xFF2196F3), // Blue
            value = 30,
            description = "Category A"
        ),
        SimplePieChartData(
            color = Color(0xFF4CAF50), // Green
            value = 20,
            description = "Category B"
        ),
        SimplePieChartData(
            color = Color(0xFFFFC107), // Amber
            value = 15,
            description = "Category C"
        ),
        SimplePieChartData(
            color = Color(0xFFE91E63), // Pink
            value = 35,
            description = "Category D"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleAnimatedPieChart(dataPoints = sampleData)

        FlowRow(
            modifier = Modifier.padding(16.dp),
        ) {
            sampleData.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                            .clip(shape = CircleShape)
                            .size(16.dp)
                            .background(it.color)
                    )
                    Text(text = it.description)
                }
            }
        }
    }
}