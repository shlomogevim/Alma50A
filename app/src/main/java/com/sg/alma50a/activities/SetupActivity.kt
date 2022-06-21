package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.activities_tt.GradePostActivity
import com.sg.alma50a.databinding.ActivitySetupBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_ALMA
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.FirestoreClass
import java.lang.reflect.Type

class SetupActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    private lateinit var binding: ActivitySetupBinding
    private var currentUser: User? = null
    var posts = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref=getSharedPreferences(SHARPREF_ALMA, Context.MODE_PRIVATE)


        setupButtons()
    }
    override fun onStart() {
        super.onStart()
        FirestoreClass().getUserDetails(this)
        posts=loadPosts()
    }

    fun loadPosts():ArrayList<Post>{
        posts.clear()
        val gson= Gson()
        val json: String? =pref.getString(Constants.SHARPREF_POSTS_ARRAY,null)
        val type: Type =object : TypeToken<ArrayList<Post>>() {}.type
        // val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
        val arr:ArrayList<Post> =gson.fromJson(json,type)
        return arr
    }
    private fun setupButtons() {
        binding.btnGradePost.setOnClickListener {
            startActivity(Intent(this, GradePostActivity::class.java))
          finish()
        }
        binding.btnGradeOrder.setOnClickListener {
           pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_GRADE).apply()
            pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, 0).apply()
            startActivity(Intent(this, MainActivity::class.java))
            //finish()
        }
        binding.btnTimeOrder.setOnClickListener {
            pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).apply()
            pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, 0).apply()
            startActivity(Intent(this, MainActivity::class.java))
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