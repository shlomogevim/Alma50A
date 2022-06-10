package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.alma50a.activities_tt.GradePostActivity
import com.sg.alma50a.databinding.ActivitySetupBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.FirestoreClass

class SetupActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    private lateinit var binding: ActivitySetupBinding
    private var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref=getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)


        setupButtons()
    }
    override fun onStart() {
        super.onStart()
        FirestoreClass().getUserDetails(this)
    }

    private fun setupButtons() {
        binding.btnGradePost.setOnClickListener {
            startActivity(Intent(this, GradePostActivity::class.java))
          //  finish()
        }
        binding.btnGradeOrder.setOnClickListener {
            pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_GRADE).apply()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.btnTimeOrder.setOnClickListener {
            pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).apply()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.btnChangeProfile.setOnClickListener {
            val intent = Intent(this, PostSettingActivity::class.java)
            intent.putExtra(Constants.USER_EXTRA, currentUser)
            startActivity(intent)
            finish()
        }
        binding.btnPassToNewPost.setOnClickListener {
            startActivity(Intent(this,GetNextPost::class.java))
            finish()
        }
    }

    fun getingUserData(user: User) {
       currentUser=user
    }
}