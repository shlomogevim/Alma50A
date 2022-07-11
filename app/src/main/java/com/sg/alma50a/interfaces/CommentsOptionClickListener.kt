package com.sg.alma50a.interfaces


import com.sg.alma50a.models.Comment


interface CommentsOptionClickListener {
    fun optionMenuClicked(comment: Comment)
}