package com.sg.alma50a.activities_tt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.R
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.databinding.ActivityGradePostBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_ALMA
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_TOTAL_POSTS
import com.sg.alma50a.utilities.Constants.USER_GRADE_AR
import com.sg.alma50a.utilities.FirestoreClass

class GradePostActivity : BaseActivity() {
    private lateinit var binding:ActivityGradePostBinding
    lateinit var pref: SharedPreferences
    lateinit var gradeHashMap: HashMap<Int,Int>
lateinit var  gson : Gson
   private var totalPostsNun=0
    private var currentPostNum=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGradePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
         pref=getSharedPreferences(Constants.SHARPREF_ALMA , Context.MODE_PRIVATE)
        totalPostsNun=pref.getInt(SHARPREF_TOTAL_POSTS,0)
         currentPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        gradeHashMap=HashMap()
        gson = Gson()
        val storeMappingString=pref.getString("SHARPREF_GRADE","oppsNotExist")
        if (storeMappingString!="oppsNotExist") {
            val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
           gradeHashMap=gson.fromJson(storeMappingString,type)
            val currentGradNum=gradeHashMap[currentPostNum]


            
            binding.etGradeNum.hint=currentGradNum.toString()

        }




        binding.btnPassPost.setOnClickListener {


         /*   if (TextUtils.isEmpty(binding.etGradeNum.text.toString().trim { it <= ' ' }) -> {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)

            false
        }*/
            val newGradePostNum=binding.etGradeNum.text.toString().toInt()
            if (newGradePostNum>=0 && newGradePostNum<=100) {
               gradeHashMap[currentPostNum] = newGradePostNum
                val hashMapString = gson.toJson(gradeHashMap)
                pref.edit().putString("SHARPREF_GRADE", hashMapString).apply()
                finish()
            }else{
                showErrorSnackBar("צריך להכניס מספר בין 0 ל 100", true)
            }

        }

    }


}

