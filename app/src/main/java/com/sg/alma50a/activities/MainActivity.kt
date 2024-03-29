package com.sg.alma50a.activities

import com.sg.alma50a.Cstom_Zoom.CenterZoomLayout
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.R
import com.sg.alma50a.adapters.PostAdapter

import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.models.Comment
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.*
import com.sg.alma50a.utilities.Constants.SHARPREF_COMMENTS_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_POSTS_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_RECOMMENDED
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import java.lang.reflect.Type

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    var posts = ArrayList<Post>()
    val comments = ArrayList<Comment>()
    private var currentUser: User? = null

    lateinit var rvPosts: RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var pref: SharedPreferences
    lateinit var gson: Gson
    var sortSystem = "NoValue"
    var currentPostNum = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gson = Gson()
        rvPosts = binding.rvPosts
        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        FirestoreClass().getUserDetails(this)
        sortSystem = pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_RECOMMENDED).toString()
        currentPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        // logi("MainActivity 57   onCreate  57            ")
    }

    override fun onResume() {
        super.onResume()
        //  logi("MainActivity onResum 61              sortSystem$sortSystem")
        posts.clear()
        posts = loadPosts()
        sortSystem = pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_RECOMMENDED).toString()
        currentPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        FirestoreClass().getUserDetails(this)
        sortPosts()
        if (currentPostNum == 0) {
                currentPostNum = posts[0].postNum
            }
            create_rvPost()
            moveIt()
      }
    fun getCurrentUser(user: User) {
       currentUser=user
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun create_rvPost() {                                     //Animation 1
        val layoutManger = CenterZoomLayout(this)
        layoutManger.orientation = LinearLayoutManager.HORIZONTAL
        layoutManger.reverseLayout = true
        // layoutManger.stackFromEnd=true
        rvPosts.layoutManager = layoutManger

        val snapHelper = LinearSnapHelper()
        rvPosts.setOnFlingListener(null)
        snapHelper.attachToRecyclerView(rvPosts)
        rvPosts.isNestedScrollingEnabled = false

        postAdapter = PostAdapter(this, posts)
        rvPosts.adapter = postAdapter
        rvPosts.setHasFixedSize(true)
        postAdapter.notifyDataSetChanged()
    }

    private fun sortPosts() {
        if (sortSystem == SHARPREF_SORT_BY_RECOMMENDED) {
            //   posts.removeAll { it.postId < 2 }
            posts.sortWith(compareByDescending({ it.postId }))                 //postId show recommended factor

        }
        if (sortSystem == SHARPREF_SORT_BY_TIME_PUBLISH) {
            posts.sortWith(compareByDescending({ it.timestamp }))
            //  logi("MainActivity in sortPosts  111       sortSystem=$sortSystem       posts.size=${posts.size}")
        }

//          persons.sortWith(compareBy({ it.name }, { it.age }))
        if (sortSystem == SHARPREF_SORT_BY_GRADE) {
            posts.removeAll({ it.grade == 0 })
        logi("MainActivity in sortPosts  120       sortSystem=$sortSystem       posts.size=${posts.size}")
            if (posts.size==0){
                sortSystem == SHARPREF_SORT_BY_RECOMMENDED
                posts.sortWith(compareByDescending({ it.postId }))
            }else{
                posts.sortWith(compareByDescending({ it.grade }))
            }
        // logi("MainActivity in sortPosts  121    sortSystem=$sortSystem       posts.size=${posts.size}")
        }
    }

    fun loadPosts(): ArrayList<Post> {
        posts.clear()
        val gson = Gson()
        val json: String? = pref.getString(SHARPREF_POSTS_ARRAY, null)
        val type: Type = object : TypeToken<ArrayList<Post>>() {}.type
        // val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
        val arr: ArrayList<Post> = gson.fromJson(json, type)
        return arr
    }

    fun loadComments(): ArrayList<Comment> {
        comments.clear()
        val gson = Gson()
        val json: String? = pref.getString(SHARPREF_COMMENTS_ARRAY, null)
        val type: Type = object : TypeToken<ArrayList<Comment>>() {}.type
        // val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
        val arr: ArrayList<Comment> = gson.fromJson(json, type)
        return arr
    }

    private fun moveIt() {
        //logi("MainActivity 129   currentPostNum=$currentPostNum")

        Handler().postDelayed(
            {
                for (counter in 0 until posts.size) {
                    if (posts[counter].postNum == currentPostNum) {
                        rvPosts.scrollToPosition(counter)
                        // logi("MainActivity 136   counter=$counter")
                    }
                }
            }, 100
        )
    }




}

/*  private fun addAnimation(pager: ViewPager2) {
        val book = BookFlipPageTransformer2()
        book.setEnableScale(true)
        book.setScaleAmountPercent(90f)
        pager.setPageTransformer(book)

        val card = CardFlipPageTransformer2()
        card.setScalable(false)
        pager.setPageTransformer(card)
    }*/
