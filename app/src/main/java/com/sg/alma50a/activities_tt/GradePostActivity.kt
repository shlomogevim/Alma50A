package com.sg.alma50a.activities_tt

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.databinding.ActivityGradePostBinding
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.FALSE
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_GRADE_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_GRADE_ZERO
import com.sg.alma50a.utilities.Constants.SHARPREF_TOTAL_POSTS_SIZE

class GradePostActivity : BaseActivity() {
    private lateinit var binding:ActivityGradePostBinding
    lateinit var pref: SharedPreferences
    lateinit var gradeHashMap: HashMap<Int,Int>
lateinit var  gson : Gson
   private var totalPostsNun=0
    private var currentPostNum=0
    private var gradeSt=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGradePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
         pref=getSharedPreferences(Constants.SHARPREF_ALMA , Context.MODE_PRIVATE)
        totalPostsNun=pref.getInt(SHARPREF_TOTAL_POSTS_SIZE,0)
         currentPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        gradeHashMap=HashMap()
        gson = Gson()

        val storeMappingString=pref.getString(SHARPREF_GRADE_ARRAY,"oppsNotExist")
//        logi("GradePostActivity 33  storeMappingString=$storeMappingString")
        if (storeMappingString!="oppsNotExist") {
            val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
           gradeHashMap=gson.fromJson(storeMappingString,type)
            val currentGradNum=gradeHashMap[currentPostNum]
            if (currentGradNum==null || currentGradNum==0){
                 gradeSt="הפוסט הזה לא מדורג אצלך עדיין ..."
            }else{
                val st1="הדירוג הנוכחי של הפוסט :"
                gradeSt="$st1 $currentGradNum"
            }
         //   logi("GradePostActivity 38  currentGradNum=$currentGradNum")
            binding.etGradeNum.hint=gradeSt
        }

        binding.btnPassPost.setOnClickListener {
            val newGradePostNum=binding.etGradeNum.text.toString().toInt()
            if (newGradePostNum>=0 && newGradePostNum<=100) {
               gradeHashMap[currentPostNum] = newGradePostNum
                if (newGradePostNum>0){
                    pref.edit().putString(SHARPREF_GRADE_ZERO, FALSE)
                }
                val hashMapString = gson.toJson(gradeHashMap)
                pref.edit().putString(SHARPREF_GRADE_ARRAY, hashMapString).apply()
              //  pref.edit().putString(SHARPREF_GRADE_ZERO,"false").apply()

                finish()
            }else{
                showErrorSnackBar("צריך להכניס מספר בין 0 ל 100", true)
            }

        }


    }


}

