/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.savedata.presentation

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.wear.compose.material.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController


class MainActivity : ComponentActivity() {
    private lateinit var mSensorManager: SensorManager
    private var mSensor : Sensor? = null
    private val HEART_SENSORS_REQUEST_CODE = 10
    var sensorData = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContent {
            WearApp(sharedPreference)
        }
    }
}

object NavRoute {
    const val SCREEN_2 = "screen2"
    const val SCREEN_3 = "screen3"
    const val DETAILSCREEN = "detailScreen/{id}"

}


@Composable
fun WearApp(sharedPreferences: SharedPreferences) {
    var editor = sharedPreferences.edit()
    editor.putString("username", "JORGE ALBERTO")
    editor.commit()

    //val sharedPref : SharedPreferences?= getSharedPreferences(Context.MODE_PRIVATE);
    //val pref: SharedPreferences = getApplicationContext<Context>()
    //.getSharedPreferences("MyPref", 0) // 0 - for private mode
    //val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)

    //var editor = pref.edit()
    //editor.putString("username","Anupam")
    //editor.putLong("l",100L)
    //editor.commit()
    //pref.getString("username","defaultName")
    //sharedPreference.getLong("l",1L)

    //val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)

    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavRoute.SCREEN_2
    ) {
        composable(NavRoute.SCREEN_2) {
            Screen2(navController, sharedPreferences)
        }
        composable(NavRoute.SCREEN_3) {
            Screen3(navController, sharedPreferences)
        }
        //composable("detailScreen/{id}") {// backStackEntry ->  backStackEntry.arguments?.getString("id")
        composable(NavRoute.DETAILSCREEN) {// backStackEntry ->  backStackEntry.arguments?.getString("id")
            detailScreen(
                id = it.arguments?.getString("id") ?: "0",
                sharedPreferences,
                navController
            )
            //detailScreen(id = it.arguments?.getString("id")!!)
            //BackHandler(true) {
            // Or do nothing
            //Timber.e("Clicked back")
            //}
        }

    }
    //return WearApp()
}

@Composable
fun Screen2(navigation: NavController, SharedPreferences: SharedPreferences) {
    /*Timer().schedule(5000) {
        navigation.navigate(NavRoute.SCREEN_3)
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            ), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Hello From Start Screen (SCREEN_2)",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navigation.navigate("screen3") }) {
        }
    }
}

@Composable
fun Screen3(navigation: NavController, SharedPreferences: SharedPreferences) {

    val items = listOf("One", "Two", "Three", "Four", "Five")
    val state = rememberPickerState(items.size)
    //val contentDescription by remember { derivedStateOf { "${state.selectedOption + 1}" } }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            text = "Selected: ${items[state.selectedOption]}"
        )
        Picker(
            modifier = Modifier.size(100.dp, 100.dp),
            state = state,
            //contentDescription = contentDescription,
        ) {
            Text(items[it], modifier = Modifier.padding(10.dp))
        }
    }

    val items2 = listOf("One", "Two", "Three", "Four", "Five")
    val state2 = rememberPickerState(items2.size)
    //val contentDescription by remember { derivedStateOf { "${state.selectedOption + 1}" } }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            text = "Selected: ${items2[state.selectedOption]}"
        )
        Picker(
            modifier = Modifier.size(100.dp, 100.dp),
            state = state2,
            //contentDescription = contentDescription,
        ) {
            Text(items[it], modifier = Modifier.padding(10.dp))
        }
    }
    //Spacer(modifier = Modifier.height(20.dp))
    //Text("Hello From Second Screen (SCREEN_3)",
    //    textAlign = TextAlign.Center,)
    //Spacer(modifier = Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(10.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        var position = state2.selectedOption
        var value = items2[position]
        Button(
            onClick = {
                navigation.navigate("detailScreen/{$value}") {
                    launchSingleTop = true
                    popUpTo("screen2") { inclusive = true }// Clear hasta la pantalla elegida
                }
            },

            //Modifier.background(color = Color.Yellow)
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Yellow,
                contentColor = Color.Red
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "Next",
                //modifier = iconModifier,
                tint = Color.DarkGray
            )
        }
    }


}

@Composable
fun detailScreen(id: String, SharedPreferences: SharedPreferences, navigation: NavController) {
    /*Timer().schedule(5000) {
        navigation.navigate(NavRoute.SCREEN_3)
    }*/
    //navigation.popBackStack()

    var user = SharedPreferences.getString("username", "defaultName")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            ), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Hello From detail Screen (detailScreen) $id",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Button(onClick = { navigation.navigate("screen3") }) {
        Text(
            "El usuario es $user",
            textAlign = TextAlign.Center,
        )
        Button(onClick = {
            var editor = SharedPreferences.edit()
            editor.remove("username")
            editor.commit()
            navigation.navigate("screen3")
        }) {
            Text("Clear user data")
        }
    }
}


