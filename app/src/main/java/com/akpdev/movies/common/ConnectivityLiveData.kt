package com.akpdev.movies.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class ConnectivityLiveData(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnectionStatus()
        }
    }

    override fun onActive() {
        super.onActive()
        updateConnectionStatus()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private fun updateConnectionStatus() {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val isConnected = activeNetworkInfo?.isConnectedOrConnecting == true
        postValue(isConnected)
    }
}
