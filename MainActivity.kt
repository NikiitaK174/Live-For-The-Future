package com.example.liveforthefuture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.liveforthefuture.ui.theme.LiveForTheFutureTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource


// 🚀 Main Activity: Handles Navigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            LiveForTheFutureTheme {
                NavHost(navController = navController, startDestination = "screen0") {
                    composable("screen0") { Screen1(navController) }
                    composable("screen1") { Screen2(navController) }
                    composable("screen2") { Screen3(navController) }
                }
            }
        }
    }
}


@Composable
fun Screen1(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFE696E)) // Background color
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally // ✅ Center content
    ) {
        // 📝 Displaying Question Cards
        QuestionCard("Have you ever been physically hit?")
        QuestionCard("Have you ever been pressured into performing sexual acts?")
        QuestionCard("Are you threatened on a daily basis?")
        QuestionCard("Do you feel that you need support from someone?")

        // 🌍 Navigation Buttons (Added Back)
        Button(onClick = { navController.navigate("screen1") }) {
            Text("Go to Screen 2")
        }

        Button(onClick = { navController.navigate("screen2") }) {
            Text("Go to Screen 3")
        }
    }
}

// 🏁 BLANK SCREEN 2
@Composable
fun Screen2(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFE696E))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ 1st White Text Box at the Top (Centered Text with Spacing)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp) // Adjust height as needed
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You are no longer alone. We are here for you",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 50.sp // ✅ Increase spacing between lines
                    )
                }
            }

            // ✅ 2nd White Text Box (Image on Left + Text in Center + Image on Right)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black) // ✅ Add Black Outline
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // ✅ Space between elements
                ) {
                    Text(
                        text = "What is your current address?",
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f) // ✅ Ensures text takes up available space
                    )
                }
            }

            // ✅ 3rd White Text Box (Same Structure with Different Content)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp) // Adjust size as needed
                    .background(Color.White)
                    .border(2.dp, Color.Black) // ✅ Add Black Outline
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // ✅ Space between elements
                ) {
                    // 📷 Left Image (Camera Icon or Another)
                    Image(
                        painter = painterResource(id = R.drawable.magnifying_glass),
                        contentDescription = "Camera Icon",
                        modifier = Modifier.size(40.dp)
                    )

                    // 🆔 Right Image (ID Card or Another)
                    Image(
                        painter = painterResource(id = R.drawable.images),
                        contentDescription = "ID Card",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You will soon receive help.",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 50.sp // ✅ Increase spacing between lines
                    )
                }



                }

            Button(onClick = { navController.navigate("screen0") }) {
                Text("Go Back to Screen 0")
            }
        }
    }
}




@Composable
fun Screen3(navController: NavController) {
    // Create state: list of 9 booleans (all false = empty)
    val glassStates = remember { mutableStateListOf(false, false, false, false, false, false, false, false, false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title (Optional)
            Text(
                text = "Drink water! Tap a glass to fill it!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 3x3 Grid using nested Rows
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        val isFull = glassStates[index]

                        // Image with click toggle
                        Image(
                            painter = painterResource(
                                id = if (isFull) R.drawable.full else R.drawable.empty
                            ),
                            contentDescription = "Glass $index",
                            modifier = Modifier
                                .size(90.dp)
                                .clickable {
                                    glassStates[index] = !isFull // toggle full/empty
                                }
                        )
                    }
                }
            }

            // 🔙 Back Button
            Button(onClick = { navController.navigate("screen0") }) {
                Text("Go Back to Screen 0")
            }
        }
    }
}



// ✅ QUESTION CARD FUNCTION (PLACED AT THE BOTTOM)
@Composable
fun QuestionCard(questionText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(0.dp), // ✅ No shadows
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(2.dp, Color.Black)
                .padding(16.dp), // ✅ Inner padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = questionText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}
