package com.sg.alma50a.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sg.alma50a.R
import com.sg.alma50a.activities.PostDetailesActivity
import com.sg.alma50a.modeles.Post
import com.sg.alma50a.post_drawing.DrawGeneralPost
import com.sg.alma50a.post_drawing.DrawPostCenter
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.POST_EXSTRA
import com.sg.alma50a.utilities.Constants.SHARPREF_ALMA
import com.sg.alma50a.utilities.UtilityPost
import java.util.*
import kotlin.collections.ArrayList





class PostAdapter( val context: Context,val posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    val util = UtilityPost()

    val base= BaseActivity()
 val  pref=context.getSharedPreferences(SHARPREF_ALMA, Context.MODE_PRIVATE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
              val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindImage(posts[position])

    }

    override fun getItemCount() = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout = itemView?.findViewById<ConstraintLayout>(R.id.itemLayout)
      //  val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)

        fun bindImage(post: Post) {
            pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, post.postNum).apply()
           DrawGeneralPost().drawPost(context,post,layout)  // onClick include in here
           // DrawPostCenter(context).drawPostFire(post,layout)
        }

    }
}




/*class PostAdapter(val viewPager: ViewPager2, val context: Context, val posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PagerViewHolder>() {

    val util = UtilityPost()
   val  pref=context.getSharedPreferences(SHARPREF_ALMA, Context.MODE_PRIVATE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PagerViewHolder(view)
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindImage(posts[position])
        prepareMoreImage(position+1)

        //---------------------
        if (position == posts.size - 2) {
            viewPager.post(run)
        }

        /*viewPager.post{
            viewPager.setCurrentItem(10,true)
        }*/
        //------------------
    }

    private fun prepareMoreImage(position: Int) {
        var pos=position

        if (pos<posts.size){
            loadImage(pos)
        }else{
            pos=0
        }
        pos++
        if (pos<posts.size){
            loadImage(pos)
        }else{
            pos=0
        }

    }

    private fun loadImage(pos: Int) {

    }


    override fun getItemCount() = posts.size




    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout = itemView?.findViewById<ConstraintLayout>(R.id.itemLayout)
      //  val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)

        fun bindImage(post: Post) {
            pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_NUM, post.postNum).apply()
             DrawGeneralPost().drawPost(context,post,layout)  // onClick include in here
        }

    }

    //-------------------
    val run = object : Runnable {            // for automate scrolling
        override fun run() {
            posts.addAll(posts)
            notifyDataSetChanged()
        }
    }
    //-------------
}*/