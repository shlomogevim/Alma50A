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
import com.sg.alma50a.adapters.PostAdapter

import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.utilities.*
import com.sg.alma50a.utilities.Constants.POST_REF
import com.sg.alma50a.utilities.Constants.POST_TIME_STAMP
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.Constants.SHARPREF_TOTAL_POSTS_SIZE
import java.lang.reflect.Type

//class MainActivity : BaseActivity(),PassToNewPostInterface {
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    var posts = ArrayList<Post>()

    lateinit var postAdapter: PostAdapter
    lateinit var pager: ViewPager2
    lateinit var gradeArray: ArrayList<Int>
    lateinit var pref: SharedPreferences
    lateinit var gradeHashMap: HashMap<Int, Int>
    lateinit var gson: Gson


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        setContentView(binding.root)
        gradeArray = arrayListOf()
        gson = Gson()

      //  posts = loadPosts()
      //  sortPosts()
        pager = binding.viewPager
        postAdapter = PostAdapter(pager, this, posts)
         pager.adapter = postAdapter
         postAdapter.notifyDataSetChanged()

       addAnimation(pager)
       moveIt()

    }


       /* Handler().postDelayed(
            {  if (!pressHelpBtn) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            }, 6000
        )*/


    override fun onResume() {
        super.onResume()

      val currentPost= pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
      //  logi("MainActivity 66    currentPost=$currentPost")
//        if (currentPost>0) {




            posts = loadPosts()
            sortPosts()
//             logi("MainActivity  79     posts[0].postNum=${posts[0].postNum}")
//             logi("MainActivity  79     posts[1].postNum=${posts[1].postNum}")
//             logi("MainActivity  79     posts[2].postNum=${posts[2].postNum}")
//             logi("MainActivity  79     posts[3].postNum=${posts[3].postNum}")
            pager = binding.viewPager

            postAdapter = PostAdapter(pager, this, posts)
            pager.adapter = postAdapter
            postAdapter.notifyDataSetChanged()

//               }
      //  addAnimation(pager)
    //    moveIt()


    }

    private fun sortPosts() {
//          persons.sortWith(compareBy({ it.name }, { it.age }))
        var sortSystem =
            pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).toString()
        //  sortSystem= SHARPREF_SORT_BY_GRADE
        logi("MainActivity in sortPosts  93       sortSystem=$sortSystem       posts.size=${posts.size}")
        if (sortSystem == SHARPREF_SORT_BY_GRADE) {
            posts.sortWith(compareByDescending({ it.grade }))
            logi("MainActivity in sortPosts  95       sortSystem=$sortSystem       posts.size=${posts.size}")
        }
        if (sortSystem == SHARPREF_SORT_BY_TIME_PUBLISH) {
            posts.sortWith(compareByDescending({ it.timestamp }))
            logi("MainActivity in sortPosts  98       sortSystem=$sortSystem       posts.size=${posts.size}")
        }
    postAdapter.notifyDataSetChanged()  //not workink
    }



    fun loadPosts(): ArrayList<Post> {
        posts.clear()
        val gson = Gson()
        val json: String? = pref.getString(Constants.SHARPREF_POSTS_ARRAY, null)
        val type: Type = object : TypeToken<ArrayList<Post>>() {}.type
        // val type = object : TypeToken<HashMap<Int?, Int?>?>() {}.type
        val arr: ArrayList<Post> = gson.fromJson(json, type)
        return arr
    }

    private fun moveIt() {
        val newPostNum = pref.getInt(SHARPREF_CURRENT_POST_NUM, 0)
        if (newPostNum>0) {
            Handler().postDelayed(
                {
                    for (counter in 0 until posts.size) {
                        if (posts[counter].postNum == newPostNum) {
                            pager.setCurrentItem(counter)
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


    /* fun downloadAllPost(): ArrayList<Post> {
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

                     pref.edit().putInt(SHARPREF_TOTAL_POSTS_SIZE,posts.size).apply()
                     retriveGradeMapFromSharPref()
                   //  sortPosts()
                     savePosts(pref,posts)
                  //   logi("MainActivity 53   posts=$posts")
                     postAdapter.notifyDataSetChanged()
                 }
             }
         return posts
     }*/
    /* fun savePosts(pref: SharedPreferences, posts: ArrayList<Post>) {
         val editor=pref.edit()
         val gson= Gson()
         val json:String=gson.toJson(posts)
         editor.putString(Constants.SHARPREF_POSTS_ARRAY,json)
         editor.apply()
       //
     }*/

    /* fun downloadAllPost(): ArrayList<Post> {
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

                     pref.edit().putInt(SHARPREF_TOTAL_POSTS_SIZE,posts.size).apply()
                     retriveGradeMapFromSharPref()
                     sortPosts()
                     postAdapter.notifyDataSetChanged()
                 }
             }
         return posts
     }*/


    /*  private fun retriveGradeMapFromSharPref() {
           val storeMappingString=pref.getString("SHARPREF_GRADE","oppsNotExist")
        // logi("MainActivity 110  storeMappingString=$storeMappingString")
          if (storeMappingString=="oppsNotExist"){
              logi("MainActivity 112  not exist")
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
      }*/


    /* private fun sortHashMap() {
         printHashMap(gradeHashMap)
         val result=gradeHashMap.toList().sortedByDescending { (_,value)->value }.toMap()
         printHashMap(result)
     }*/

    /*  private fun printHashMap(map:Map<Int, Int>) {
          var size=0
          for (entery in map){
              if (size<6) {
                  println("gg, Key: " + entery.key +" Value: " + entery.value)
                  size++
              }
          }
          println("gg,  ---------------")

      }*/


}


/*private fun createGradeArray() {
    for (index in 0 until posts.size){
        gradeArray[index]=0
    }

 val gradeStringArray=ArrayList<String>()
    for (index in 0 until posts.size){
        gradeStringArray[index]="$index+aa"
        val prsonal=Personal(index, index+22)
        personalArrayList.add(prsonal)
    }


     val categoryList=gradeStringArray.toCollection(ArrayList())
      val set=HashSet<String>()
  set.addAll(categoryList)
    val pref=getSharedPreferences(SHARPREF_POST_NUM,Context.MODE_PRIVATE)
        pref.edit().putStringSet(SHARPREF_GRAD_ARRAY,set).commit()


}*/


/*Set<String> set = myScores.getStringSet("key", null);

//Set the values
Set<String> set = new HashSet<String>();
set.addAll(listOfExistingScores);
scoreEditor.putStringSet("key", set);
scoreEditor.commit();*/

/*private fun saveData() {
    val pref=getSharedPreferences(SHARPREF_POST_NUM,Context.MODE_PRIVATE)
    val editor= pref.edit()
    val gson = Gson()
    val json=gson.toJson(personalArrayList)
    editor.putString(SHARPREF_GRAD_ARRAY,json)
    editor.apply()

 *//*   val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(courseModalArrayList)

        editor.putString("courses", json)
        editor.apply()
        Toast.makeText(this, "Saved to Shared preferences. ", Toast.LENGTH_SHORT).show()*//*
    }*/
/*private fun loadData() {
    personalArrayList=ArrayList()
    val pref=getSharedPreferences(SHARPREF_POST_NUM,Context.MODE_PRIVATE)
    val gson = Gson()
    val json=pref.getString(SHARPREF_GRAD_ARRAY,null)
    val type = object : TypeToken<ArrayList<Personal>>() {}.type
    var newArrayList=gson.fromJson<Personal>(json,type)
    if (newArrayList==null){
        newArrayList= ArrayList()
    }

//        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPreferences.getString("courses", null)

//        val type = object : TypeToken<ArrayList<CourseModal?>?>() {}.type
//        courseModalArrayList = gson.fromJson(json, type)
//
//
//        if (courseModalArrayList == null) {
//            courseModalArrayList = ArrayList()
//        }
//        fun saveData() {
//
//
//        }


}*/


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
