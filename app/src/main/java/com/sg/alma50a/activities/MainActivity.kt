package com.sg.alma50a.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.sg.alma50a.adapters.PostAdapter
import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.BookFlipPageTransformer2
import com.sg.alma50a.utilities.CardFlipPageTransformer2
import com.sg.alma50a.utilities.Constants.POST_REF
import com.sg.alma50a.utilities.Constants.POST_TIME_STAMP
import com.sg.alma50a.utilities.UtilityPost
import java.util.ArrayList

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    val posts = ArrayList<Post>()
    lateinit var postAdapter: PostAdapter
    lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logi("MainActivity 37  ")
        val posts = downloadAllPost()
      //  logi("MainActivity 39     =======>  posts[0]=${posts[0]}  ")
        pager = binding.viewPager
        postAdapter = PostAdapter(pager, this, posts)
        pager.adapter = postAdapter
        addAnimation(pager)
    }



    fun downloadAllPost(): ArrayList<Post> {
        posts.clear()
        FirebaseFirestore.getInstance().collection(POST_REF)
            .orderBy( POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                logi("  MainActivity 53    ===>value= ${value} " )
                if (value != null) {
                    for (doc in value.documents) {
                       // logi("  MainActivity 56    ===>doc= ${doc} " )
                        val post = util.retrivePostFromFirestore(doc)
                        posts.add(post)
                    }
                // logi("  MainActivity 56    ===>posts[0]= ${posts[0]} " )

                    postAdapter.notifyDataSetChanged()
                }
            }
        return posts
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


    private fun to_Automate_Scrolling_addThisInto_onCreate(pager: ViewPager2) {
        lateinit var sliderHandler: Handler
        lateinit var sliderRun: Runnable

        pager.clipToPadding = false
        pager.clipChildren = false
        pager.offscreenPageLimit = 3
        pager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val comPostPageTarnn = CompositePageTransformer()
        comPostPageTarnn.addTransformer(MarginPageTransformer(40))
        comPostPageTarnn.addTransformer { page, position ->
            val r: Float = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        pager.setPageTransformer(comPostPageTarnn)
        sliderHandler = Handler()
        sliderRun = Runnable {
            pager.currentItem = pager.currentItem + 1
        }
        pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRun)
                    sliderHandler.postDelayed(sliderRun, 2000)
                }
            })
    }
}