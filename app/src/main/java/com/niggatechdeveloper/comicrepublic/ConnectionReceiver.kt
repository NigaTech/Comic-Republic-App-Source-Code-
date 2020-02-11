package com.niggatechdeveloper.comicrepublic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectionReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context, p1: Intent?) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo

        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (connectionReceiverListener != null){
            connectionReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectionRecieverListener{

        fun onNetworkConnectionChanged(isConnected: Boolean)

    }

    companion object{
        var connectionReceiverListener: ConnectionRecieverListener? = null

        val isConnected: Boolean
        get() {
            val cm = MyApplications.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo

            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
        }
    }

}