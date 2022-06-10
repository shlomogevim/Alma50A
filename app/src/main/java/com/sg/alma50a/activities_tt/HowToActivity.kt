package com.sg.alma50a.activities_tt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.alma50a.R
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.databinding.ActivityHowToBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.FirestoreClass

class HowToActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHowToBinding
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHowToBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUserID = FirestoreClass().getCurrentUserID()
        if (currentUserID != null) {
            FirestoreClass().getUserDetails(this)
        }
    }
    fun getingUserData(user: User) {
        currentUser = user
        setText()
    }

    private fun setText() {
        var name = ""
        if (currentUser != null) {
            name = "${currentUser!!.userName} ${currentUser!!.lastName} "
        } else {
            name = "אורח"
        }
        val st="$name"
        binding.tvText1.text = st
        binding.tvText2.text = "הגעת למסך האיך ל..."
        val totalString=howToString1()
        binding.tvHowToExplanation.text=totalString
    }
    private fun howToString1(): String{
        val st= "בוא נניח שאתם רוצים להעיר משהו על פוסט כלשהו," +"\n"+

                "  x@xx.com"+"\n"+
        "------------------------------------------------"
        return st
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}