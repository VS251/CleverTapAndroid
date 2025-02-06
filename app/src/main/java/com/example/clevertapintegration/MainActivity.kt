package com.example.clevertapintegration

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clevertap.android.sdk.CleverTapAPI
import com.example.clevertapintegration.ui.theme.CleverTapIntegrationTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM Token", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM Token", "Token: $token")
            }


        val clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)
        clevertapDefaultInstance?.pushEvent("Product Viewed")

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnTestEvent = findViewById<Button>(R.id.btnTestEvent)

        // Handle LOGIN Button Click
        btnLogin.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            // Add code to handle login here
            val profileUpdate = HashMap<String, Any>()
            profileUpdate["Name"] = name
            profileUpdate["Email"] = email
            // Send to CleverTap
            CleverTapAPI.getDefaultInstance(applicationContext)?.onUserLogin(profileUpdate)
        }

        // Handle TEST EVENT Button Click
        btnTestEvent.setOnClickListener {
            // Push a test event to CleverTap
            CleverTapAPI.getDefaultInstance(applicationContext)?.pushEvent("TEST")
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CleverTapIntegrationTheme {
        Greeting("Android")
    }
}