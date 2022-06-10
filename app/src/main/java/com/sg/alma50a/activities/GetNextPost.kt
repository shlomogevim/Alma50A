package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sg.alma50a.databinding.ActivityGetNextPostBinding
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants.SHARPREF_ALMA
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM

class GetNextPost() : BaseActivity() {
    private lateinit var binding: ActivityGetNextPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetNextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPassPost.setOnClickListener {
            val newPostNum = binding.etPassNum.text.toString().toInt()
            val pref = getSharedPreferences(SHARPREF_ALMA, Context.MODE_PRIVATE)
            pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, newPostNum).apply()
            startActivity(Intent(this, MainActivity::class.java))
           // finish()
        }
    }


}