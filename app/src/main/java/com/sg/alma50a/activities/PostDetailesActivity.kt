package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.R
import com.sg.alma50a.adapters.CommentAdapter
import com.sg.alma50a.databinding.ActivityPostDetailesBinding
import com.sg.alma50a.interfaces.CommentsOptionClickListener
import com.sg.alma50a.models.Comment
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.*
import com.sg.alma50a.utilities.Constants.COMMEND_TIME_STAMP
import com.sg.alma50a.utilities.Constants.COMMENT_ID
import com.sg.alma50a.utilities.Constants.COMMENT_POST_NUM_STRING
import com.sg.alma50a.utilities.Constants.COMMENT_REF
import com.sg.alma50a.utilities.Constants.COMMENT_TEXT
import com.sg.alma50a.utilities.Constants.SHARPREF_ALMA
import com.sg.alma50a.utilities.Constants.SHARPREF_COMMENTS_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import java.lang.reflect.Type


class PostDetailesActivity : BaseActivity(), CommentsOptionClickListener {

    private lateinit var binding: ActivityPostDetailesBinding
 //   val currentUser = FirebaseAuth.getInstance().currentUser
   var util = UtilityPost()
    var currentUser: User? = null
    var textViewArray = ArrayList<TextView>()
    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    var comments = ArrayList<Comment>()
   lateinit var currentPost: Post
    var message = ""
    lateinit var newUtil1: NewUtilities
    lateinit var pref: SharedPreferences
  //  var currentUserName=""
   //var currentPostNumString=""


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostDetailesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirestoreClass().getUserDetails(this)

        newUtil1 = NewUtilities(this)
        pref = getSharedPreferences(SHARPREF_ALMA, Context.MODE_PRIVATE)
       // currentUserName = pref.getString(SHARPREF_CURRENT_USER_NAME, null).toString()
        currentPost = loadCurrentPost()
       // currentPostNumString=currentPost.postNum.toString()

        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, currentPost.postNum).apply()
        drawHeadline()
        create_commentsRv()
        btnSetting()
        retriveComments()


        binding.postCommentText.addTextChangedListener(textWatcher)
    }

    fun getUserNameSetting(user: User) {
            currentUser=user
        if (currentUser==null){
            binding.nameCurrentUserName.setText("אורח")
        }else {
            binding.nameCurrentUserName.setText(currentUser!!.userName)
        }
    }

    private fun retriveComments() {
        //  logi(" PostDetail 124")
        FirebaseFirestore.getInstance().collection(COMMENT_REF)
           .orderBy(COMMEND_TIME_STAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                        if (comment.postNumString==currentPost.postNum.toString()){
                            comments.add(comment)
                        }
                    }
                commentAdapter.notifyDataSetChanged()
                }
            }
    }
    private fun sendComment() {
        hideKeyboard()
        val commentText = binding.postCommentText.text.toString().trim { it <= ' ' }
        newUtil1.saveCommentInFirestore(commentText,currentPost.postNum.toString())
        binding.postCommentText.text.clear()
    }

    fun loadCurrentPost(): Post {
        val gson = Gson()
        val json: String? = pref.getString(SHARPREF_CURRENT_POST, null)
        val type: Type = object : TypeToken<Post>() {}.type
        val post: Post = gson.fromJson(json, type)
        return post
    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (currentUser == null) {
                hideKeyboard()
                val st1 = "צריך ל  "
                val st2 = "הכנס"
                val st3 = "  כדי לכתוב הערה."
                val st12 = "\""
                message = st1 + st12 + st2 + st12 + st3
                showErrorSnackBar(message, true)
            }
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
    }


    private fun btnSetting() {
        //   logi("PostDetaileActivity 79 =====>  currentPost=$currentPost ")

        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        /*  binding.profileBtn.setOnClickListener {
              //  logi("PostDetaileActivity  in profileBtn 84   =====>  currentPost=$currentPost ")
              val intent = Intent(this, PostSettingActivity::class.java)
              intent.putExtra(USER_EXTRA, currentUser)
              startActivity(intent)
          }*/

        binding.profileImageComment.setOnClickListener {
            // logi("PostDetaileActivity  in profileImage 90   =====>  currentPost=$currentPost ")
            press_on_comment_icon()
        }
        binding.postNumber.setOnClickListener {
            startActivity(Intent(this, GetNextPost::class.java))
            finish()
        }
        binding.settingUpBtn.setOnClickListener {
            startActivity(Intent(this, SetupActivity::class.java))
            finish()

        }
    }
    private fun press_on_comment_icon() {
        if (currentUser == null) {
            hideKeyboard()
            val st1 = "צריך ל  "
            val st2 = "הכנס"
            val st3 = "  כדי לשלוח הערה."
            val st12 = "\""
            message = st1 + st12 + st2 + st12 + st3
            showErrorSnackBar(message, true)
        } else {
            val commentText = binding.postCommentText.text.toString()
            if (commentText == "") {
                message = " היי , קודם תכתוב משהו בהערה ואחר כך תלחץ ..."
                showErrorSnackBar(message, true)
            } else {
                sendComment()
            }
        }
    }
    private fun create_commentsRv() {
        commentsRV = binding.rvPost
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        commentsRV.layoutManager = layoutManger
        commentAdapter = CommentAdapter(this,comments, this)
        commentsRV.adapter = commentAdapter
        commentAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val inputeManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputeManager.isAcceptingText) {
            inputeManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun optionMenuClicked(comment: Comment) {

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.option_menu, null)
        val deleteBtn = dialogView.findViewById<Button>(R.id.optionDelelBtn)
        val editBtn = dialogView.findViewById<Button>(R.id.optionEditBtn)
        builder.setView(dialogView)
            .setNegativeButton("Cancel") { _, _ -> }
        val ad = builder.show()
        deleteBtn.setOnClickListener {
            util.deleteComment(comment)
            finish()
        }
        editBtn.setOnClickListener {
            val intent = Intent(this, UpdateCommentActivity::class.java)
            intent.putExtra(COMMENT_POST_NUM_STRING, comment.postNumString)
            intent.putExtra(COMMENT_ID, comment.commntId)
            intent.putExtra(COMMENT_TEXT, comment.text)
            startActivity(intent)
            finish()
        }
    }

    private fun drawHeadline() {
        val st = "  פוסט מספר: " + currentPost.postNum.toString()
        binding.postNumber.text = st
        // logi("PostDetailsActivity  233  post=$currentPost    \n post.postText.size= ${currentPost.postText.size}")
        drawPostText()
    }
    private fun sortComment(comments1: ArrayList<Comment>): ArrayList<Comment> {
        var arr=ArrayList<Comment>()
        comments.clear()
        for (index  in 0 until comments1.size){
            /* logi("PostDetailActivity  76      index=$index    comments1[index].postNumString===>> ${comments1[index].postNumString}     currentPostNumString=$currentPostNumString        /n " +
                     " ${comments1[index].postNumString==currentPostNumString}")*/
            if (comments1[index].postNumString==currentPost.postNum.toString()){
                arr.add(comments1[index])
            }
        }
        return arr
    }
    private fun createComments() {
     /*   comments1.clear()
        comments.clear()
        comments1 = loadComments()
        show_comments1()

       logi("PostDetailActivity  77      currentPostNumString=$currentPostNumString    \n")
        for (index  in 0 until comments1.size){
            *//* logi("PostDetailActivity  76      index=$index    comments1[index].postNumString===>> ${comments1[index].postNumString}     currentPostNumString=$currentPostNumString        /n " +
                     " ${comments1[index].postNumString==currentPostNumString}")*//*

            if (comments1[index].postNumString==currentPostNumString){
                comments.add(comments1[index])
            }
        }
        show_comments()*/
    }

   /* override fun onResume() {
        super.onResume()
            createComments()

        commentAdapter = CommentAdapter(this,comments, this)
//        val layoutManger = LinearLayoutManager(this)
//        layoutManger.reverseLayout = true
//        commentsRV.layoutManager = layoutManger
//        commentsRV.adapter = commentAdapter
        commentAdapter.notifyDataSetChanged()


    }*/

    private fun show_comments1() {
//        for (index in 0 until comments1.size){
//           logi("PostDetailActivity 105 coments1   index=$index         ===> comment1  = ${comments1[index].postNumString}   \n")
//        }
    }
    private fun show_comments() {
        for (index in 0 until comments.size){
            logi("PostDetailActivity 110 coment  index=$index         ===> comment  = ${comments[index].postNumString}   \n")
        }
    }



    fun loadComments( ): ArrayList<Comment> {
        comments.clear()
        val gson = Gson()
        val json: String? = pref.getString(Constants.SHARPREF_COMMENTS_ARRAY, null)
        val type: Type = object : TypeToken<ArrayList<Comment>>() {}.type
        var arr: ArrayList<Comment> = gson.fromJson(json, type)
        return arr
    }



    fun saveComments() {
        val editor=pref.edit()
        val gson= Gson()
        val json:String=gson.toJson(comments)
        editor.putString(SHARPREF_COMMENTS_ARRAY,json)
        editor.apply()
        //
    }


    private fun sortComments(): MutableList<Comment> {

       // var arr=ArrayList<Comment>()
        var arr=comments.toMutableList()
     //  arr.filter {  it.postNumString==currentPost.postId }
         //   arr.sortWith(compareByDescending ({ it.timestamp}))
        return arr

    }
    /*      persons.sortWith(compareBy({ it.name }, { it.age }))
        if (sortSystem == SHARPREF_SORT_BY_GRADE) {
            posts.sortWith(compareByDescending({ it.grade }))
          // logi("MainActivity in sortPosts  107       sortSystem=$sortSystem       posts.size=${posts.size}")
        }*/





    /* private fun sendComment() {
         hideKeyboard()
         FirebaseFirestore.getInstance().collection(POST_REF)
             .document(currentPost.postNum.toString())
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {

                     val post = util.retrivePostFromFirestore(task.result)
                     //   val email = binding.etEmail.text.toString().trim { it <= ' ' }
                     val commentText = binding.postCommentText.text.toString().trim { it <= ' ' }
                     currentUser?.let { util.createComment(post, commentText, it) }
                     newUtil1.createNewComment(commentText)
                     // logi("PostDetailesActivity  135    commentText=$commentText  ")
                     binding.postCommentText.text.clear()
                 }
             }
     }*/
    /*  private fun sendComment() {
        hideKeyboard()
        FirebaseFirestore.getInstance().collection(POST_REF)
            .document(currentPost.postNum.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val post = util.retrivePostFromFirestore(task.result)
                    //   val email = binding.etEmail.text.toString().trim { it <= ' ' }
                    val commentText = binding.postCommentText.text.toString().trim { it <= ' ' }
                    currentUser?.let { util.createComment(post, commentText, it) }
                    // logi("PostDetailesActivity  135    commentText=$commentText  ")
                    binding.postCommentText.text.clear()
                }
            }
    }*/



    private fun createTextViewArray() {
        with(binding) {
            textViewArray = arrayListOf(
                tvPost1,
                tvPost2,
                tvPost3,
                tvPost4,
                tvPost5,
                tvPost6,
                tvPost7,
                tvPost8,
                tvPost9
            )
        }

    }

    private fun drawPostText() {
        if (currentPost.postText.size == 1) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
        }
        if (currentPost.postText.size == 2) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
        }
        if (currentPost.postText.size == 3) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
        }
        if (currentPost.postText.size == 4) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
        }
        if (currentPost.postText.size == 5) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost5.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
            binding.tvPost5.text = currentPost.postText[4]
        }
        if (currentPost.postText.size == 6) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost5.visibility = View.VISIBLE
            binding.tvPost6.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
            binding.tvPost5.text = currentPost.postText[4]
            binding.tvPost6.text = currentPost.postText[5]
        }
        if (currentPost.postText.size == 7) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost5.visibility = View.VISIBLE
            binding.tvPost6.visibility = View.VISIBLE
            binding.tvPost7.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
            binding.tvPost5.text = currentPost.postText[4]
            binding.tvPost6.text = currentPost.postText[5]
            binding.tvPost7.text = currentPost.postText[6]
        }
        if (currentPost.postText.size == 8) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost5.visibility = View.VISIBLE
            binding.tvPost6.visibility = View.VISIBLE
            binding.tvPost7.visibility = View.VISIBLE
            binding.tvPost8.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
            binding.tvPost5.text = currentPost.postText[4]
            binding.tvPost6.text = currentPost.postText[5]
            binding.tvPost7.text = currentPost.postText[6]
            binding.tvPost8.text = currentPost.postText[7]
        }
        if (currentPost.postText.size == 9) {
            binding.tvPost1.visibility = View.VISIBLE
            binding.tvPost2.visibility = View.VISIBLE
            binding.tvPost3.visibility = View.VISIBLE
            binding.tvPost4.visibility = View.VISIBLE
            binding.tvPost5.visibility = View.VISIBLE
            binding.tvPost6.visibility = View.VISIBLE
            binding.tvPost7.visibility = View.VISIBLE
            binding.tvPost8.visibility = View.VISIBLE
            binding.tvPost9.visibility = View.VISIBLE
            binding.tvPost1.text = currentPost.postText[0]
            binding.tvPost2.text = currentPost.postText[1]
            binding.tvPost3.text = currentPost.postText[2]
            binding.tvPost4.text = currentPost.postText[3]
            binding.tvPost5.text = currentPost.postText[4]
            binding.tvPost6.text = currentPost.postText[5]
            binding.tvPost7.text = currentPost.postText[6]
            binding.tvPost8.text = currentPost.postText[7]
            binding.tvPost9.text = currentPost.postText[8]
        }
    }




}

