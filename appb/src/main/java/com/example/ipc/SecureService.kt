package com.example.appb

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log

class SecureService : Service() {
    private val binder = object : ISecureService.Stub() {
        @Throws(RemoteException::class)
        override fun sendSecureRequest(encryptedData: String): String {
            return try {
                val decrypted = ServiceCrypto.decrypt(encryptedData)
                Log.d("SecureService", "Received: $decrypted")

                val response = "Processed: $decrypted"
                
                ServiceCrypto.encrypt(response)
            } catch (e: Exception) {
                Log.e("SecureService", "Error processing request", e)
                throw RemoteException("Processing failed: ${e.message}")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("SecureService", "Service bound")
        return binder
    }
}