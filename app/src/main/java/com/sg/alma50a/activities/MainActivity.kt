package com.sg.alma50a.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sg.alma50a.adapters.PostAdapter

import com.sg.alma50a.databinding.ActivityMainBinding
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.models.Person
import com.sg.alma50a.models.Personal
import com.sg.alma50a.utilities.*
import com.sg.alma50a.utilities.Constants.POST_REF
import com.sg.alma50a.utilities.Constants.POST_TIME_STAMP
import com.sg.alma50a.utilities.Constants.SHARPREF_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_GRADE
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.Constants.SHARPREF_TOTAL_POSTS

//class MainActivity : BaseActivity(),PassToNewPostInterface {
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val util = UtilityPost()
    val posts = ArrayList<Post>()

    lateinit var postAdapter: PostAdapter
    lateinit var pager: ViewPager2
    lateinit var gradeArray:ArrayList<Int>
    lateinit var pref:SharedPreferences
    lateinit var gradeHashMap: HashMap<Int,Int>
    lateinit var  gson : Gson

    //private var personalArrayList: ArrayList<Personal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
         pref=getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        setContentView(binding.root)
        gradeArray= arrayListOf()
        gson=Gson()

        val posts = downloadAllPost()

        pager = binding.viewPager
        postAdapter = PostAdapter(pager, this, posts)
        pager.adapter = postAdapter

       addAnimation(pager)
      //createGradeArray()
    }

    override fun onResume() {
        super.onResume()
     val  newPostNum1=pref.getInt(SHARPREF_NUM,0)
        if (newPostNum1>0) {
              moveIt(newPostNum1)
        }
    }

    private fun moveIt(index: Int) {

        Handler().postDelayed(
            {

              //  saveData()
                for (counter in 0 until posts.size){
                    if (posts[counter].postNum==index){
                        pager.setCurrentItem(counter)
                    }
                }
        },3000)

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
                    postAdapter.notifyDataSetChanged()
                    pref.edit().putInt(SHARPREF_TOTAL_POSTS,posts.size).apply()
                    retriveGradeMapFromSharPref()
                    sortPosts()

                }
            }
        return posts
    }

    private fun sortPosts() {
//          persons.sortWith(compareBy({ it.name }, { it.age }))
        val sortSystem=pref.getString(SHARPREF_SORT_TOTAL,"NoSystem")
      if (sortSystem== SHARPREF_SORT_BY_GRADE) {
          posts.sortWith(compareByDescending({ it.grade }))
          postAdapter.notifyDataSetChanged()
      }
        if (sortSystem== SHARPREF_SORT_BY_TIME_PUBLISH) {
            posts.sortWith(compareByDescending({ it.timestamp}))
            postAdapter.notifyDataSetChanged()
        }

        //logi("MainAvtivity 109")
        //val cloneNames = names.map{it.copy()}

      /*  printPosts("post",posts)
        val posts1=posts.map{it.copy()}
        posts1.sortedWith(compareBy({it.grade}))
        printPosts("post1",posts)*/

        //sortHashMap()

    }

    private fun sortHashMap() {
        printHashMap(gradeHashMap)
        val result=gradeHashMap.toList().sortedByDescending { (_,value)->value }.toMap()
        printHashMap(result)
    }

    private fun printHashMap(map:Map<Int, Int>) {
        var size=0
        for (entery in map){
            if (size<6) {
                println("gg, Key: " + entery.key +" Value: " + entery.value)
                size++
            }
        }
        println("gg,  ---------------")

    }


    fun littleSort(){



        /*val result = capitals.toList().sortedBy { (_, value) -> value}.toMap()
        for (entry in result) {
            print("gg, Key: " + entry.key)
            println("gg,  Value: " + entry.value)
        }*/







      /*  val persons = arrayListOf(
            Person("Olivia", 25),
            Person("George", 15),
            Person("Olivia", 20),
            Person("Harry", 10)
        )

        for (person in persons) {
            println(person)
        }
        println("gg, --------------------------")
        persons.sortWith(compareBy({ it.name }, { it.age }))

        for (person in persons) {
            println(person)
        }*/
    }

    private fun printPosts(st:String,postsx: ArrayList<Post>) {
        logi("MainActivity 124   $st")
        for (index in 0 until 5){
            val num=postsx[index].postNum
            val value = postsx[index].grade
         //   logi("MainActivity 135   num=$num     value=$value")

        }
    }


    private fun retriveGradeMapFromSharPref() {
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
