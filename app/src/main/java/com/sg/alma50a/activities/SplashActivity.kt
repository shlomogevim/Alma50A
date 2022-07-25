package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.animation.AlphaAnimation
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
import com.sg.alma50a.utilities.Constants.FALSE
import com.sg.alma50a.utilities.Constants.NOT_EXIST
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_USER_NAME
import com.sg.alma50a.utilities.Constants.SHARPREF_GRADE_ARRAY
import com.sg.alma50a.utilities.Constants.SHARPREF_GRADE_ZERO
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_RECOMMENDED
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_BY_TIME_PUBLISH
import com.sg.alma50a.utilities.Constants.SHARPREF_SORT_TOTAL
import com.sg.alma50a.utilities.Constants.SHARPREF_SPLASH_SCREEN_DELAY
import com.sg.alma50a.utilities.Constants.TRUE
import com.sg.alma50a.utilities.Constants.USER_REF
import com.sg.alma50a.utilities.FirestoreClass
import com.sg.alma50a.utilities.UtilityPost

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    var currentUser: User? = null
    lateinit var pref : SharedPreferences
    var pressHelpBtn = false
   // var currentUseName=""
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
        FirestoreClass().getUserDetails(this)
        initData()

        //delayInMicroSecond=1_000


        getHeadLines()

        binding.btnHelp.setOnClickListener {
            pressHelpBtn = true
            startActivity(Intent(this, HelpActivity::class.java))
        }

       downloadAllPost()
        retriveComments()
        pauseIt()
    }

    fun getingUserData(user: User) {
        currentUser = user
//        currentUser=null
   //     logi("SplashActivity   81      currentUser = $currentUser  "  )
    }
    private fun initData() {
        pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, 0).apply()
//        pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_RECOMMENDED).apply()
        pref.edit().putString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).apply()
        delayInMicroSecond= pref.getInt(SHARPREF_SPLASH_SCREEN_DELAY,10)*1000
    }




    private fun getHeadLines() {
        setFirstHello()
        timerWorks()

    }
    private fun setFirstHello() {
        val name:String=getUserName()
        val helloSt= "מה קורה " +"\n"+
                    "" +"\n"+
                    name+"\n"+
                    "" +"\n"+
                    "מה הענינים " +"\n"+
                    "" +"\n"+
                    "איך ככה " +"\n"+
                    ""
        binding.tvText1.text=helloSt
    }

    private fun timerWorks() {
//          lottie.animate().translationY(1400f).setDuration(1000).setStartDelay(4000)
       // starteAnimateLottie()
        val animate1= AlphaAnimation(0f, 1.0f)
        animate1.duration=5000
        binding.lottie.startAnimation(animate1)


        val st1="אל תתיאש עוד: "+"\n"+"\n"
        val st2="   שניות   "+"\n"+"\n"+
                 "  תתחיל האפליקציה לעבוד "
      binding.lottie.playAnimation()

        timer= object :CountDownTimer(delayInMicroSecond.toLong(),1000){



            override fun onTick(remaning: Long) {
                //if (remaning==(delayInMicroSecond.toLong())){
                //    binding.lottie.playAnimation()
              //  }


                //  binding.lottie.pauseAnimation()


                val totalMessage=st1+(remaning /1000).toString()+st2
                binding.tvText2.text=totalMessage
            }
            override fun onFinish() { }
        }
    }

    private fun starteAnimateLottie() {
        val animate1= AlphaAnimation(0.2f, 1.0f)
        animate1.duration=2000


        /*AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
animation1.setDuration(1000);
animation1.setStartOffset(5000);
animation1.setFillAfter(true);
iv.startAnimation(animation1);*/
    }

    private fun getUserName(): String {

        if (currentUser != null) {
             return "${currentUser!!.userName} ${currentUser!!.lastName} "
        } else {
            return "אורח"
        }

    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
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

//    private fun saveUserName() {
//        var currentUserID = FirestoreClass().getCurrentUserID()
////        logi("SplashActivity 98    currentUserID=$currentUserID")
////currentUserID=""
//
//        if (currentUserID !="") {
//            //  FirestoreClass().getUserDetails(this)
//            FirebaseFirestore.getInstance().collection(USER_REF).document(currentUserID)
//                .get()
//                .addOnSuccessListener { document ->
//                    val user = document.toObject(User::class.java)!!
//                    currentUser=user
//                    currentUseName=user.userName
//                    pref.edit().putString(SHARPREF_CURRENT_USER_NAME,"${user.userName}").apply()
//                }
//        }else{
//            pref.edit().putString(SHARPREF_CURRENT_USER_NAME,"אורח").apply()
//        }
//    }

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
        var storeMappingString=pref.getString(SHARPREF_GRADE_ARRAY,"oppsNotExist")

  //  storeMappingString="oppsNotExist"    //**********
       //  logi("Splash 159      storeMappingString=$storeMappingString")
        if (storeMappingString=="oppsNotExist"){
            val gradeMap:HashMap<Int,Int> = hashMapOf()
            for (index in 0 until posts.size){
                val post=posts[index]
                gradeMap[post.postNum]=0
                post.grade=0
            }
            val gson=Gson()
            val hashMapString = gson.toJson(gradeMap)
            pref.edit().putString(SHARPREF_GRADE_ARRAY, hashMapString).apply()
            pref.edit().putString(SHARPREF_GRADE_ZERO, TRUE)
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
            val bo=chkPostsGradeIsZero()
           if (bo){
               pref.edit().putString(SHARPREF_GRADE_ZERO, TRUE).apply()
           }else{
               pref.edit().putString(SHARPREF_GRADE_ZERO, FALSE).apply()
           }
        }
    }

    private fun chkPostsGradeIsZero(): Boolean {
        for (item in posts){
            if (item.grade>0){
                return false
            }
        }
         return true
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
        pref.edit().putString(SHARPREF_GRADE_ZERO,"true").apply()
        for (post in posts){
            if (post.grade>0){
                pref.edit().putString(SHARPREF_GRADE_ZERO,"false").apply()
                break
            }
        }
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



    private fun pauseIt() {
//        val sortSystem =  pref.getString(SHARPREF_SORT_TOTAL, SHARPREF_SORT_BY_TIME_PUBLISH).toString()
        Handler().postDelayed(
            {  if (!pressHelpBtn) {
                startActivity(Intent(this, MainActivity::class.java))
            }
           }, delayInMicroSecond.toLong()
//           }, 0
        )
    }
}

