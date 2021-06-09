package com.ozgenur.odev.intkontrol

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ozgenur.odev.R
import kotlinx.android.synthetic.main.int_kontrol.*

class IntKontrol {
    companion object{
        fun customErrorDialog(activity: Activity, error: String): Dialog?{
            if (error != null && !activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setContentView(R.layout.int_kontrol)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()

                val errorOk = dialog.error_ok
                val errorMessage = dialog.error_message

                var err = error

                val boolean1 = error.contains("connect")
                val boolean2 = error.contains("fail")
                val boolean3 = error.contains("port")
                val boolean4 = error.contains("time")
                val boolean5 = error.contains("ms")
                val boolean6 = error.contains("unreachable")

                if (boolean4 || boolean5) {
                    err =  "Sunucudan Gerekli Yanıt Alınamadı.."
                }else if (boolean1 || boolean2 || boolean3 || boolean6) {
                    err = "Lütfen internet bağlantınızın olduğundan emin olunuz..."
                }

                errorMessage.text = err
                errorOk.setOnClickListener {
                    if (activity.toString().contains("SplashActivity")) {
                        activity.finish()
                    } else {
                        dialog.dismiss()
                    }
                }
                return dialog
            }
            return null
        }

        fun hasInternetConnection(activity: Activity): Boolean {
            val connectivityManager = activity.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }
    }
}