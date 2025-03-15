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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
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

// 🏁 BLANK SCREEN 1
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // Background color
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the Image
        Image(
            painter = painterResource(id = R.drawable.glasses),
            contentDescription = "Water Glasses Image",
            modifier = Modifier.size(400.dp)
        )



        Button(onClick = { navController.navigate("screen0") }) {
            Text("Go Back to Screen 0")
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // ✅ Fix alignment
        ) {
            // White Background Box for the Question Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp), // Add padding inside the box
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = questionText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, // ✅ Black text for contrast
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            // Row for Buttons
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* Handle correct answer */ },
                    colors = ButtonDefaults.buttonColors(Color.Green),
                    shape = RectangleShape // ✅ Makes button square
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.check_mark),
                        contentDescription = "Water Glasses Image",
                        modifier = Modifier.size(25.dp)
                    )
                }

                Button(
                    onClick = { /* Handle incorrect answer */ },
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    shape = RectangleShape // ✅ Makes button square
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.x_sign),
                        contentDescription = "Water Glasses Image",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

