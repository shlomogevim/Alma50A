package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import com.sg.alma50a.HelpActivity


import com.sg.alma50a.databinding.ActivitySplashBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.FirestoreClass

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    var currentUser: User? = null
    lateinit var pref : SharedPreferences
    var pressHelpBtn = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
         pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        val currentUserID = FirestoreClass().getCurrentUserID()
        if (currentUserID != null) {
            FirestoreClass().getUserDetails(this)
        }

        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, 0).apply()

        binding.btnHelp.setOnClickListener {
            pressHelpBtn = true
            startActivity(Intent(this, HelpActivity::class.java))

        }
            pauseIt()
    }

    private fun setText() {
        var name = ""
        if (currentUser != null) {
            name = "${currentUser!!.userName} ${currentUser!!.lastName} "
        } else {
            name = "אורח"
        }
        // logi("Splash 55       currentUser = $currentUser       name=$name"         )

        binding.tvText1.text = "ברוך הבא "
        binding.tvText2.text = name
//        binding.tvText3.text= "לכבוד יום ההולדת 2 שלי"
//        binding.tvText4.text="אני רוצה לספר לך"
//       binding.tvText5.text=" מה שלימדו אותנו היום בגן ..."
    }


    fun getingUserData(user: User) {
        currentUser = user
        setText()
        // logi("Splash 67        currentUser = $currentUser      "         )
    }

    private fun pauseIt() {
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
                if (!pressHelpBtn) {
                    startActivity(Intent(this, MainActivity::class.java))
                                   }




            }, 6000
        )
    }
}

/* private fun setText() {
//        val font= FontFamilies()
//        val fontAddress = font.getFamilyFont(103)
//        binding.tvText1.typeface = ResourcesCompat.getFont(this, fontAddress)
        binding.tvText1.text="זה מה שלימדו אותנו היום בגן ..."
    }
   */