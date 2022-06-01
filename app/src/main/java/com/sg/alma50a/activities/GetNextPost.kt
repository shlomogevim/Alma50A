package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.sg.alma50a.R
import com.sg.alma50a.databinding.ActivityGetNextPostBinding
import com.sg.alma50a.interfaces.PassToNewPostInterface
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_NUM

/*class CommentAdapter( val comments: ArrayList<Comment>,
    val commentOptionListener: CommentsOptionClickListener
) :*/

//class GetNextPost(val passNumLister:PassToNewPostInterface) : BaseActivity() {
class GetNextPost() : BaseActivity() {
    private lateinit var binding: ActivityGetNextPostBinding
    var newNum=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGetNextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPassPost.setOnClickListener {

              val newPostNum=binding.etPassNum.text.toString().toInt()
          //  passNumLister.passToNewPostFunction(335)
          //   passNumLister.passToNewPostFunction(335)
           // logi("GetNextPost 25   newNum=$newNum ")




                val pref=getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
                 pref.edit().putInt(SHARPREF_NUM,newPostNum).apply()
                startActivity(Intent(this,MainActivity::class.java))
            finish()


        }
    }


}