package com.example.clevertapintegration
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.clevertap.android.sdk.CleverTapAPI

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Received token: $token")

        // Send token to CleverTap
        CleverTapAPI.getDefaultInstance(applicationContext)?.pushFcmRegistrationId(token, true)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM Message", "Message received: ${remoteMessage.data}")

        // Handle CleverTap Push Notification
        if (remoteMessage.data.isNotEmpty()) {
            val extras = Bundle()
            for ((key, value) in remoteMessage.data) {
                extras.putString(key, value)
            }

            val info = CleverTapAPI.getNotificationInfo(extras)
            if (info.fromCleverTap) {
                CleverTapAPI.createNotification(applicationContext, extras)
            }
        }
    }
}
