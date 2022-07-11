package com.sg.alma50a.activities_tt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.adapters.CommentAdapter
import com.sg.alma50a.databinding.ActivityCommentsScreenBinding
import com.sg.alma50a.interfaces.CommentsOptionClickListener
import com.sg.alma50a.models.Comment
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.UtilityPost

class CommentsScreenActivity : BaseActivity(), CommentsOptionClickListener {
    lateinit var binding:ActivityCommentsScreenBinding
    var util = UtilityPost()
    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    var comments = ArrayList<Comment>()
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCommentsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

       pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
       retriveComments()
        create_commentsRv()
    }
    private fun create_commentsRv() {
        commentsRV = binding.rvCommentsScreen
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        commentsRV.layoutManager = layoutManger
        commentAdapter = CommentAdapter(this,comments, this)
        commentsRV.adapter = commentAdapter
        commentAdapter.notifyDataSetChanged()
    }
    private fun retriveComments() {
        //  logi(" PostDetail 124")
        FirebaseFirestore.getInstance().collection(Constants.COMMENT_REF)
            .orderBy(Constants.COMMEND_TIME_STAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                     //   if (comment.postNumString==currentPostName){
                            comments.add(comment)
                      //  }

                    }
                    commentAdapter.notifyDataSetChanged()
                }
            }
    }

   /* fun loadCurrentPost(): Post {
        val gson = Gson()
        val json: String? = pref.getString(Constants.SHARPREF_CURRENT_POST, null)
        val type: Type = object : TypeToken<Post>() {}.type
        val post: Post = gson.fromJson(json, type)
        return post
    }*/

    override fun optionMenuClicked(comment: Comment) {

        val newPostNum=comment.postNumString.toInt()
        logi("CommentsScreenActivity 99   newPostNum=$newPostNum")
        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, newPostNum).apply()
       // startActivity(Intent(this, PostDetailesActivity::class.java))
        startActivity(Intent(this, MainActivity::class.java))
        finish()


    }
}