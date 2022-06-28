package com.sg.alma50a.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.alma50a.databinding.ActivityUpdateCommentBinding
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants.COMMENT_ID
import com.sg.alma50a.utilities.Constants.COMMENT_LIST
import com.sg.alma50a.utilities.Constants.COMMENT_POST_NUM_STRING
import com.sg.alma50a.utilities.Constants.COMMENT_REF
import com.sg.alma50a.utilities.Constants.COMMENT_TEXT
import com.sg.alma50a.utilities.UtilityPost

class UpdateCommentActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateCommentBinding
    var commentPostId=""
    var commentId=""
    var commentText=""
    val util= UtilityPost()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        commentId=intent.getStringExtra(COMMENT_ID).toString()
        commentPostId=intent.getStringExtra(COMMENT_POST_NUM_STRING).toString()
        commentText=intent.getStringExtra(COMMENT_TEXT).toString()
        binding.updateCommentTxt.setText(commentText)
       // logi("UpdateCommentActivity  36      commentId=$commentId   commentPostId=$commentPostId  commentText=$commentText")

        binding.updateCommentBtn.setOnClickListener {
            val newText=binding.updateCommentTxt.text.toString()
          //  logi("UpdateCommentActivity  40   newText=$newText   commentId=$commentId   commentPostId=$commentPostId  commentText=$commentText")
            FirebaseFirestore.getInstance().collection(COMMENT_REF).document(commentId)
                .update(COMMENT_TEXT,newText)
                .addOnSuccessListener {
                    hideKeyboard()
                 //   logi("UpdateCommentActivity 45     success ")
                    finish()
                }
                .addOnFailureListener {
                  //  logi("UpdateCommentActivity 49     success ")
                }
        }
    }
    private fun hideKeyboard() {
        val inputeManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputeManager.isAcceptingText) {
            inputeManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}