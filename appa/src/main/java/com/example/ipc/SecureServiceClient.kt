package com.example.ipc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.appb.ISecureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecureServiceClient(private val context: Context) {
    private var service: ISecureService? = null
    private var bound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = ISecureService.Stub.asInterface(binder)
            bound = true
            Log.d("SecureIPC", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            service = null
            Log.d("SecureIPC", "Service disconnected")
        }
    }

    fun bind() {
        Intent().apply {
            component = ComponentName(
                "com.example.appb",
                "com.example.appb.SecureService"
            )
            context.bindService(this, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun sendRequest(data: String, callback: (String) -> Unit) {
        if (!bound) {
            Log.e("SecureIPC", "Service not bound")
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val encrypted = CryptoHelper.encrypt(data)
                val response = service?.sendSecureRequest(encrypted)
                response?.let {
                    withContext(Dispatchers.Main) {
                        callback(CryptoHelper.decrypt(it))
                    }
                }
            } catch (e: Exception) {
                Log.e("SecureIPC", "Request failed", e)
                withContext(Dispatchers.Main) {
                    callback("Error: ${e.message}")
                }
            }
        }
    }

    fun unbind() {
        if (bound) {
            context.unbindService(connection)
            bound = false
        }
    }
}