package com.sg.alma50a.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.alma50a.R
import com.sg.alma50a.adapters.PostAdapter
import com.sg.alma50a.adapters.ViewPagerAdapter
import com.sg.alma50a.databinding.ActivityMainPostFragmentBinding
import com.sg.alma50a.fragment.FirstFragment
import com.sg.alma50a.fragment.SecondFragment
import com.sg.alma50a.fragment.ThirdFragment
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.UtilityPost

class MainActivityPostFragment : BaseActivity() {
    private lateinit var binding:ActivityMainPostFragmentBinding
    private lateinit var viewPager:ViewPager2
    private lateinit var fragments:ArrayList<Fragment>
    val posts = java.util.ArrayList<Post>()
    val util = UtilityPost()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainPostFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragmentsArrayList()
        viewPager=binding.viewPager
        val adapter=ViewPagerAdapter(fragments,this)

       viewPager.adapter=adapter
    }

    private fun setFragmentsArrayList() {
       fragments= arrayListOf(
           FirstFragment(),
           SecondFragment(),
           ThirdFragment()
       )
    }

    fun downloadAllPost(): java.util.ArrayList<Post> {
        posts.clear()

        FirebaseFirestore.getInstance().collection(Constants.POST_REF)
            .orderBy(Constants.POST_TIME_STAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                logi("  MainActivity 53    ===>value= ${value} " )
                if (value != null) {
                    for (doc in value.documents) {
                        // logi("  MainActivity 56    ===>doc= ${doc} " )
                        val post = util.retrivePostFromFirestore(doc)
                        posts.add(post)
                    }
                    // logi("  MainActivity 56    ===>posts[0]= ${posts[0]} " )

                    //postAdapter.notifyDataSetChanged()
                }
            }
        return posts
    }
}

/*
    override fun onBackPressed() {
        if (viewPager.currentItem==0){
            super.onBackPressed()
        }else{
            viewPager.currentItem=viewPager.currentItem-1
        }
    }
}*/