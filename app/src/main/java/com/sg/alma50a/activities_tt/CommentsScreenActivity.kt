package com.sg.alma50a.activities_tt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.R
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.activities.PostDetailesActivity
import com.sg.alma50a.adapters.CommentAdapter
import com.sg.alma50a.databinding.ActivityCommentsScreenBinding
import com.sg.alma50a.databinding.ActivityPostDetailesBinding
import com.sg.alma50a.interfaces.CommentsOptionClickListener
import com.sg.alma50a.modeles.Comment
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.NewUtilities
import com.sg.alma50a.utilities.UtilityPost
import java.lang.reflect.Type

class CommentsScreenActivity : BaseActivity(), CommentsOptionClickListener {
    lateinit var binding:ActivityCommentsScreenBinding
    val currentUser = FirebaseAuth.getInstance().currentUser
    var util = UtilityPost()
    var textViewArray = ArrayList<TextView>()
    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    var comments = ArrayList<Comment>()
   // lateinit var currentPost: Post
    var message = ""
    lateinit var newUtil1: NewUtilities
   lateinit var pref: SharedPreferences
 //   var currentUserName=""
  //  var currentPostName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCommentsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newUtil1 = NewUtilities(this)
       pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
      //  currentUserName = pref.getString(Constants.SHARPREF_CURRENT_USER_NAME, null).toString()
       // currentPost = loadCurrentPost()
    //    currentPostName=currentPost.postNum.toString()

      //  pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, currentPost.postNum).apply()
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