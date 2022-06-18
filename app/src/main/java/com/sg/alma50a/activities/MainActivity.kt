package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.adapters.PostAdapter
import com.sg.alma50a.adapters.PostAdapterGrade

import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.utilities.*
import com.sg.alma50a.utilities.Constants.POST_REF
import com.sg.alma50a.utilities.Constants.POST_TIME_STAMP
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_POSTS_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.Constants.SHARPREF_TOTAL_POSTS_SIZE
import java.lang.reflect.Type

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    var posts = ArrayList<Post>()


    //   lateinit var pager: ViewPager2

    lateinit var rvPosts: RecyclerView
    lateinit var postAdapter: PostAdapter


    lateinit var gradeArray: ArrayList<Int>
    lateinit var pref: SharedPreferences
    lateinit var gradeHashMap: HashMap<Int, Int>
    lateinit var gson: Gson
    var sortSystem = "NoValue"
    var currentPost = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gson = Gson()

        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        sortSystem = pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).toString()
        currentPost = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        logi("MainActivity onCreate 60            sortSystem$sortSystem")

        posts = loadPosts()
        sortPosts()
        create_rvPost()

    }

    /* Handler().postDelayed(
            {  if (!pressHelpBtn) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            }, 6000
        )*/


    override fun onResume() {
        super.onResume()

        logi("MainActivity onResum 107                  sortSystem$sortSystem")
        posts = loadPosts()
        sortPosts()
        create_rvPost()
    }

    private fun create_rvPost() {
        rvPosts = binding.rvPosts
        postAdapter = PostAdapter(this, posts)
        val layoutManger = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //   layoutManger.reverseLayout = true
        rvPosts.layoutManager = layoutManger
        rvPosts.adapter = postAdapter
        rvPosts.setHasFixedSize(true)
        postAdapter.notifyDataSetChanged()

    }

    private fun sortPosts() {
//          persons.sortWith(compareBy({ it.name }, { it.age }))
        if (sortSystem == SHARPREF_SORT_BY_GRADE) {
            posts.sortWith(compareByDescending({ it.grade }))
            logi("MainActivity in sortPosts  150       sortSystem=$sortSystem       posts.size=${posts.size}")
        }
        if (sortSystem == SHARPREF_SORT_BY_TIME_PUBLISH) {
            posts.sortWith(compareByDescending({ it.timestamp }))
            logi("MainActivity in sortPosts  154       sortSystem=$sortSystem       posts.size=${posts.size}")
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

    private fun moveIt() {
        val newPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        if (newPostNum > 0) {
            Handler().postDelayed(
                {
                    for (counter in 0 until posts.size) {
                        if (posts[counter].postNum == newPostNum) {
                            //pager.setCurrentItem(counter)
                        }
                    }
                }, 100
            )
        }
    }

    private fun addAnimation(pager: ViewPager2) {
        val book = BookFlipPageTransformer2()
        book.setEnableScale(true)
        book.setScaleAmountPercent(90f)
        pager.setPageTransformer(book)

        val card = CardFlipPageTransformer2()
        card.setScalable(false)
        pager.setPageTransformer(card)
    }

}