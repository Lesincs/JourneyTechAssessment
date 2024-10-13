package com.lesincs.journeytechassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.lesincs.journeytechassessment.common.navigation.AppGraph
import com.lesincs.journeytechassessment.common.ui.theme.JourneyTechAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JourneyTechAssessmentTheme {
                AppGraph(modifier = Modifier.fillMaxSize())
            }
        }
    }
}