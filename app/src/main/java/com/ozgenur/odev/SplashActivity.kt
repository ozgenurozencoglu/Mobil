package com.ozgenur.odev

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ozgenur.odev.intkontrol.IntKontrol

class SplashActivity : AppCompatActivity() {
    var SPLASH_TIME_OUT =3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slapsh)
        supportActionBar?.hide()
        if (IntKontrol.hasInternetConnection(this)) {
            loadSplashScreen()
        } else {
            IntKontrol.customErrorDialog(this, "İnternet  Bağlantınızı Kontrol Ediniz...")
        }

    }
    private fun loadSplashScreen(){
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())

    }

}


