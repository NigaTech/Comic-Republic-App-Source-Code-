package com.niggatechdeveloper.comicrepublic

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "FCM Service"

    override
    fun onMessageReceived(remoteMessage: RemoteMessage){
        Log.d(TAG, "From: " + remoteMessage!!.from)
        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)

        val intent = Intent(this@MyFirebaseMessagingService,SnapsActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.putExtra( "fcm_message",remoteMessage.notification?.body!!)
        startActivity(intent)
    }
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
         sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }

}