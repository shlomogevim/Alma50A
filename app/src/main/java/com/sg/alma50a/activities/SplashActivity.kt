package com.sg.alma50a.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat


import com.sg.alma50a.R
import com.sg.alma50a.databinding.ActivitySplashBinding
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.FirestoreClass
import com.sg.alma50a.utilities.FontFamilies

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setText()
        pauseIt()

    }

    private fun setText() {
        val font= FontFamilies()
        val fontAddress = font.getFamilyFont(103)
        binding.tvAppName.typeface = ResourcesCompat.getFont(this, fontAddress)

        binding.tvAppName.textSize= 22F
        binding.tvAppName.text="זה מה שלימדו אותנו היום בגן ..."
    }
    private fun pauseIt() {

        /*    var currentUserID = FirestoreClass().getCurrentUserID()
            logi("splash 41       currentUserID ===>currentUser=$currentUserID  ")*/


        Handler().postDelayed(
            {
              //  var currentUserID = FirestoreClass().getCurrentUserID()

              //  logi("SplashAvtivity 49  \n     currentUserID  ===> $currentUserID  ")
               //logi("SplashAvtivity 50 ")

                /*   currentUserID=""
                   FirebaseAuth.getInstance().signOut()*/

                /*  if (currentUserID.isNotEmpty()) {
              startActivity(Intent(this, MainActivityAppShop::class.java))
          } else{
              startActivity(Intent(this, LoginActivity::class.java))
          }*/

                startActivity(Intent(this,MainActivity::class.java))

                finish()
            },2
        )
    }
}