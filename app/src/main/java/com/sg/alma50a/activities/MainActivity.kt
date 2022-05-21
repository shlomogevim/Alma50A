package com.sg.alma50a.activities

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.alma50a.adapters.InfiniteRecyclerAdapter
import com.sg.alma50a.adapters.PostAdapter

import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.models.Sample
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.BookFlipPageTransformer2
import com.sg.alma50a.utilities.CardFlipPageTransformer2
import com.sg.alma50a.utilities.Constants.POST_REF
import com.sg.alma50a.utilities.Constants.POST_TIME_STAMP
import com.sg.alma50a.utilities.UtilityPost

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    val posts = ArrayList<Post>()
    lateinit var postAdapter: PostAdapter
    lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        logi("MainActivity 32   stam  ")
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
            .orderBy(POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
//                logi("  MainActivity 47    ===>value= ${value} ")
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

}



/*
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var pager: ViewPager2
  //  lateinit var postAdapter: InfiniteRecyclerAdapter
    lateinit var postAdapter: PostAdapter
    private var posts=ArrayList<Post>()
    private var sampleList: MutableList<Sample> = mutableListOf()
    val util = UtilityPost()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val posts = downloadAllPost()
      // getSampleData()


        pager = binding.viewPager
         //postAdapter = InfiniteRecyclerAdapter(sampleList)
         postAdapter = PostAdapter(pager,this,posts)
        pager.adapter =postAdapter
        pager.currentItem = 1

        onInfinitePageChangeCallback(posts.size + 2)
    }

private fun getSampleData() {
    sampleList.add(Sample(1, "#91C555"))
    sampleList.add(Sample(2, "#F48E37"))
    sampleList.add(Sample(3, "#FF7B7B"))
}

    fun downloadAllPost(): MutableList<Post> {
        posts.clear()
        var  index=0
        FirebaseFirestore.getInstance().collection(POST_REF)
            .orderBy( POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
             //  logi("  MainActivity 73    ===>value= ${value} " )
                if (value != null) {
                    for (doc in value.documents) {
                        index++
                      //  logi("  MainActivity 76    ===>doc= ${doc} " )
                        val post = util.retrivePostFromFirestore(doc)
                        if (index<4) {
                            posts.add(post)
                           *//* if (index==1 || index==3){
                                posts.add(post)
                            }*//*
                        }

                    }
                        //  postAdapter.notifyDataSetChanged()
                }
            }
        return posts
    }


    private fun onInfinitePageChangeCallback(listSize: Int) {
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (pager.currentItem) {
                        listSize - 1 -> pager.setCurrentItem(1, false)
                        0 ->pager.setCurrentItem(listSize - 2, false)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position != 0 && position != listSize - 1) {
                  // pageIndicatorView.setSelected(position-1)
                }
            }
        })
    }

}*/

/*class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var pager: ViewPager2
    lateinit var postAdapter: InfiniteRecyclerAdapter
    private var posts: MutableList<Post> = mutableListOf()
    val util = UtilityPost()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val posts = downloadAllPost()
        pager = binding.viewPager
         postAdapter = InfiniteRecyclerAdapter(posts)
        pager.adapter =postAdapter
        pager.currentItem = 1

        onInfinitePageChangeCallback(posts.size + 2)
    }
    fun downloadAllPost(): MutableList<Post> {
        posts.clear()
        var  index=0
        FirebaseFirestore.getInstance().collection(POST_REF)
            .orderBy( POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
             //  logi("  MainActivity 73    ===>value= ${value} " )
                if (value != null) {
                    for (doc in value.documents) {
                        index++
                      //  logi("  MainActivity 76    ===>doc= ${doc} " )
                        val post = util.retrivePostFromFirestore(doc)
                        if (index<4) {
                            posts.add(post)
                           *//* if (index==1 || index==3){
                                posts.add(post)
                            }
                        }

                    }
                        //  postAdapter.notifyDataSetChanged()
                }
            }
        return posts
    }


    private fun onInfinitePageChangeCallback(listSize: Int) {
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (pager.currentItem) {
                        listSize - 1 -> pager.setCurrentItem(1, false)
                        0 ->pager.setCurrentItem(listSize - 2, false)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position != 0 && position != listSize - 1) {
                  // pageIndicatorView.setSelected(position-1)
                }
            }
        })
    }

}
*/









   /* private fun to_Automate_Scrolling_addThisInto_onCreate(pager: ViewPager2) {
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
    }*/
