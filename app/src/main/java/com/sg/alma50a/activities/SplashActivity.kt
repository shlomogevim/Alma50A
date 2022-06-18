package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.HelpActivity
import com.sg.alma50a.adapters.PostAdapter


import com.sg.alma50a.databinding.ActivitySplashBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.FirestoreClass
import com.sg.alma50a.utilities.UtilityPost
import java.lang.reflect.Type

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    var currentUser: User? = null
    lateinit var pref : SharedPreferences
    var pressHelpBtn = false

    var posts = ArrayList<Post>()
//    lateinit var postAdapter: PostAdapter
 //  lateinit var pager: ViewPager2
    lateinit var gradeArray:ArrayList<Int>
   lateinit var gradeHashMap: HashMap<Int,Int>
    lateinit var  gson : Gson
//   var sortSystem=""
    val util = UtilityPost()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gradeArray= arrayListOf()
        gson=Gson()

        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, 0).apply()
        pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).apply()
        //  pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_GRADE).apply()

        val currentUserID = FirestoreClass().getCurrentUserID()
        if (currentUserID != null) {
            FirestoreClass().getUserDetails(this)
        }

        binding.btnHelp.setOnClickListener {
            pressHelpBtn = true
            startActivity(Intent(this, HelpActivity::class.java))
        }

        downloadAllPost()
        pauseIt()

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
                    savePosts(pref,posts)
                }
            }
        return posts
    }
    private fun retriveGradeMapFromSharPref() {
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
    fun savePosts(pref: SharedPreferences, posts: ArrayList<Post>) {

        val editor=pref.edit()
        val gson= Gson()
        val json:String=gson.toJson(posts)
        editor.putString(Constants.SHARPREF_POSTS_ARRAY,json)
        editor.apply()
        //
    }
    fun getingUserData(user: User) {
        currentUser = user
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
            }, 1000
        )
    }

}

