package com.martinscomic.comicrepublic

import android.app.Application

class MyApplications: Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionRecieverListener){
        ConnectionReceiver.connectionReceiverListener = listener
    }
    companion object{
        @get:Synchronized
        lateinit var instance: MyApplications
    }
}