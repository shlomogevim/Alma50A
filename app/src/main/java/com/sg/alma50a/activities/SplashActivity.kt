package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.HelpActivity


import com.sg.alma50a.databinding.ActivitySplashBinding
import com.sg.alma50a.models.Comment
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_USER_NAME
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_RECOMMENDED
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.Constants.SHARPREF_SPLASH_SCREEN_DELAY
import com.sg.alma50a.utilities.Constants.USER_REF
import com.sg.alma50a.utilities.FirestoreClass
import com.sg.alma50a.utilities.UtilityPost

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    var currentUser: User? = null
    lateinit var pref : SharedPreferences
    var pressHelpBtn = false
    var currentUseName=""
    var delayInMicroSecond=0
    lateinit var timer: CountDownTimer
    var posts = ArrayList<Post>()
    val comments = ArrayList<Comment>()
    lateinit var gradeArray:ArrayList<Int>
   lateinit var gradeHashMap: HashMap<Int,Int>
    val util = UtilityPost()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gradeArray= arrayListOf()

        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, 0).apply()
        pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_RECOMMENDED).apply()
       delayInMicroSecond= pref.getInt(SHARPREF_SPLASH_SCREEN_DELAY,8)*1000
     //  delayInMicroSecond= 0
        getHeadLine()
        saveUserName()
        binding.btnHelp.setOnClickListener {
            pressHelpBtn = true
            startActivity(Intent(this, HelpActivity::class.java))
        }

       downloadAllPost()
        retriveComments()
     pauseIt()
    }

    private fun getHeadLine() {
        val st1="אל תתיאש עוד "
        val st2="  שניות "
        var st=""
        timer= object :CountDownTimer(delayInMicroSecond.toLong(),1000){
            override fun onTick(remaning: Long) {
              st=st1+(remaning /1000).toString()+st2
                binding.tvText1.text=st
            }

            override fun onFinish() {
//                binding.tvText1.text="טטאאח ...
            }
        }
        binding.tvText2.text="האפליקציה תתחיל לעבוד ..."
    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private fun saveUserName() {
        var currentUserID = FirestoreClass().getCurrentUserID()
//        logi("SplashActivity 98    currentUserID=$currentUserID")
//currentUserID=""

        if (currentUserID !="") {
            //  FirestoreClass().getUserDetails(this)
            FirebaseFirestore.getInstance().collection(USER_REF).document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)!!
                    currentUser=user
                    currentUseName=user.userName
                    pref.edit().putString(SHARPREF_CURRENT_USER_NAME,"${user.userName}").apply()
                }
        }else{
            pref.edit().putString(SHARPREF_CURRENT_USER_NAME,"אורח").apply()
        }
    }

    private fun retriveComments() {
        //  logi(" PostDetail 124")
        comments.clear()
        FirebaseFirestore.getInstance().collection(Constants.COMMENT_REF)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                        comments.add(comment)
                    }
                    saveComments()

                }
            }
    }

    fun downloadAllPost(): ArrayList<Post> {
        posts.clear()
        FirebaseFirestore.getInstance().collection(Constants.POST_REF)
           // .orderBy(Constants.POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (doc in value.documents) {
                        val post = util.retrivePostFromFirestore(doc)
                        posts.add(post)
                    }

                    pref.edit().putInt(Constants.SHARPREF_TOTAL_POSTS_SIZE,posts.size).apply()
                    retriveGradeMapFromSharPref()
                  //  sortPosts()
                    savePosts()
                }
            }
        return posts
    }
    private fun retriveGradeMapFromSharPref() {
        val gson=Gson()
        val storeMappingString=pref.getString("SHARPREF_GRADE","oppsNotExist")
       // logi("Splash 95  storeMappingString=$storeMappingString")
        if (storeMappingString=="oppsNotExist"){
            val gradeMap:HashMap<Int,Int> = hashMapOf()
            for (index in 0 until posts.size){
                val post=posts[index]
                gradeMap[post.postNum]=0
                post.grade=0
            }
            val gson=Gson()
            val hashMapString = gson.toJson(gradeMap)
            pref.edit().putString("SHARPREF_GRADE", hashMapString).apply()
        }
        else{
            //  logi("MainActivity 123  exist")
            val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
            gradeHashMap=gson.fromJson(storeMappingString,type)
            for (entery in   gradeHashMap ){
                // logi("MainActivity 127  key=${entery.key}   value=${entery.value}")
                val post:Post=findPost(entery.key)
                post.grade=entery.value
            }
        }
    }
    private fun findPost(key: Int): Post {
        val post=Post()
        for (post in posts){
            if (post.postNum==key){
                return post
            }
        }
        return post
    }
    fun savePosts() {
        val editor=pref.edit()
        val gson= Gson()
        val json:String=gson.toJson(posts)
        editor.putString(Constants.SHARPREF_POSTS_ARRAY,json)
        editor.apply()
        //
    }

    fun saveComments() {
        val editor=pref.edit()
        val gson= Gson()
        val json:String=gson.toJson(comments)
        editor.putString(Constants.SHARPREF_COMMENTS_ARRAY,json)
        editor.apply()
        //
    }
    fun getingUserData(user: User) {
       // currentUser = user
        setText()
        // logi("Splash 67        currentUser = $currentUser      "         )
    }
    private fun setText() {
        var name = ""
        if (currentUser != null) {
            name = "${currentUser!!.userName} ${currentUser!!.lastName} "
        } else {
            name = "אורח"
        }

        binding.tvText1.text = "ברוך הבא "
        binding.tvText2.text = name
    }

    private fun pauseIt() {
//        val sortSystem =  pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).toString()
        Handler().postDelayed(
            {  if (!pressHelpBtn) {
                startActivity(Intent(this, MainActivity::class.java))
            }
           }, delayInMicroSecond.toLong()
//            }, 0
        )
    }
}

