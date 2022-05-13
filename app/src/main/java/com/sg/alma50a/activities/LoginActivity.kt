package com.sg.alma50a.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.alma50a.R
import com.sg.alma50a.modeles.User

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun userLoggedInSuccess(user: User) {

    }

    fun hideProgressDialog() {

    }
}