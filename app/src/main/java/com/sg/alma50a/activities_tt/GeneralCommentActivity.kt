package com.sg.alma50a.activities_tt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.adapters.CommentAdapter
import com.sg.alma50a.databinding.ActivityGeneralCommentBinding
import com.sg.alma50a.interfaces.CommentsOptionClickListener
import com.sg.alma50a.models.Comment
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.NewUtilities
import com.sg.alma50a.utilities.UtilityPost

class GeneralCommentActivity : BaseActivity(), CommentsOptionClickListener {
private lateinit var binding: ActivityGeneralCommentBinding
    val currentUser = FirebaseAuth.getInstance().currentUser
    var util = UtilityPost()
    lateinit var newUtil1: NewUtilities
    lateinit var generalCommentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    var comments = ArrayList<Comment>()
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGeneralCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        newUtil1 = NewUtilities(this)
        retriveGeneralComments()
        create_commentsRv()
        setupButtom()
    }

    private fun setupButtom() {
        binding.generalProfileImageComment.setOnClickListener {
            // logi("PostDetaileActivity  in profileImage 90   =====>  currentPost=$currentPost ")
            press_on_general_comment_icon()
        }
    }
    private fun press_on_general_comment_icon() {
        var  message=""
        if (currentUser == null) {
            hideKeyboard()
            val st1 = "צריך ל  "
            val st2 = "הכנס"
            val st3 = "  כדי לשלוח הערה."
            val st12 = "\""
             message = st1 + st12 + st2 + st12 + st3
            showErrorSnackBar(message, true)
        } else {
            val commentText = binding.generalPostCommentText.text.toString()
            if (commentText == "") {
                message = " היי , קודם תכתוב משהו בהערה ואחר כך תלחץ ..."
                showErrorSnackBar(message, true)
            } else {
                sendGeneralComment()
            }
        }
    }

    private fun sendGeneralComment() {
        hideKeyboard()
        val commentText = binding.generalPostCommentText.text.toString().trim { it <= ' ' }
        newUtil1.saveGeneralCommentInFirestore(commentText)
        binding.generalPostCommentText.text.clear()
    }

    private fun hideKeyboard() {
        val inputeManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputeManager.isAcceptingText) {
            inputeManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun create_commentsRv() {
        generalCommentsRV = binding.rvCommentsScreenGeneral
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        generalCommentsRV.layoutManager = layoutManger
        commentAdapter = CommentAdapter(this,comments, this)
        generalCommentsRV.adapter = commentAdapter
        commentAdapter.notifyDataSetChanged()
    }
    private fun retriveGeneralComments() {
        FirebaseFirestore.getInstance().collection(Constants.COMMENT_REF)
            .orderBy(Constants.COMMEND_TIME_STAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {

                        val comment = util.retriveCommentFromFirestore(doc)
                         if (comment.index=="1"){
                            // logi("GeneralCommentsActivity 104   in     comment.index=${comment.index}")
                             comments.add(comment)
                      }
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
        pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, newPostNum).apply()
        // startActivity(Intent(this, PostDetailesActivity::class.java))
        startActivity(Intent(this, MainActivity::class.java))
        finish()


    }
}