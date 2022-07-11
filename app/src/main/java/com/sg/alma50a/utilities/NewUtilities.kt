package com.sg.alma50a.utilities

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.alma50a.models.Comment
import com.sg.alma50a.utilities.Constants.COMMEND_INDEX
import com.sg.alma50a.utilities.Constants.COMMEND_TIME_STAMP
import com.sg.alma50a.utilities.Constants.COMMENT_ID
import com.sg.alma50a.utilities.Constants.COMMENT_POST_NUM_STRING
import com.sg.alma50a.utilities.Constants.COMMENT_TEXT
import com.sg.alma50a.utilities.Constants.COMMENT_USER_ID
import com.sg.alma50a.utilities.Constants.COMMENT_USER_NAME
import com.sg.alma50a.utilities.Constants.COMMENT_REF

class NewUtilities(val context: Context) {
    val pref = context.getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
    val currentUserName = pref.getString(Constants.SHARPREF_CURRENT_USER_NAME, null).toString()
   // val currentPostNum = pref.getInt(Constants.SHARPREF_CURRENT_POST_NUM, 0)

    val currentUser = FirebaseAuth.getInstance().currentUser

    fun saveCommentInFirestore(commentText: String,currentPostName:String) {
       //  logi("UtilityPost 298     commentText=$commentText")
        val data = HashMap<String, Any>()
        data[COMMENT_ID] = "1"
        data[COMMENT_POST_NUM_STRING] = currentPostName
        data[COMMENT_TEXT] = commentText
        data[COMMENT_USER_NAME] = currentUserName
        data[COMMENT_USER_ID] = currentUser?.uid.toString()
        data[COMMEND_TIME_STAMP] = FieldValue.serverTimestamp()
        data[COMMEND_INDEX]="0"

        val ref = FirebaseFirestore.getInstance().collection(COMMENT_REF)
        ref.add(data)
            .addOnSuccessListener {
                data[Constants.COMMENT_ID] = it.id
                ref.document(it.id).update(data)
            }
    }
    fun saveGeneralCommentInFirestore(commentText: String) {
        //  logi("UtilityPost 298     commentText=$commentText")
        val data = HashMap<String, Any>()
        data[COMMENT_ID] = "1"
        data[COMMENT_TEXT] = commentText
        data[COMMENT_USER_NAME] = currentUserName
        data[COMMENT_USER_ID] = currentUser?.uid.toString()
        data[COMMEND_TIME_STAMP] = FieldValue.serverTimestamp()
        data[COMMEND_INDEX]="1"

        val ref = FirebaseFirestore.getInstance().collection(COMMENT_REF)
        ref.add(data)
            .addOnSuccessListener {
                data[Constants.COMMENT_ID] = it.id
                ref.document(it.id).update(data)
            }
    }
    fun saveCommentInFirestoreGeneral(commentText: String,currentPostName:String) {
        //  logi("UtilityPost 298     commentText=$commentText")
        val data = HashMap<String, Any>()
        data[COMMENT_ID] = "1"
        data[COMMENT_POST_NUM_STRING] = currentPostName
        data[COMMENT_TEXT] = commentText
        data[COMMENT_USER_NAME] = currentUserName
        data[COMMENT_USER_ID] = currentUser?.uid.toString()
        data[COMMEND_TIME_STAMP] = FieldValue.serverTimestamp()
        data[COMMEND_INDEX] ="1"

        val ref = FirebaseFirestore.getInstance().collection(COMMENT_REF)
        ref.add(data)
            .addOnSuccessListener {
                data[Constants.COMMENT_ID] = it.id
                ref.document(it.id).update(data)
            }
    }

    fun createNewComment(commentText: String, postNumString: String): Comment {
        val comment =
            Comment("1", postNumString, commentText, currentUserName, "stam", Timestamp.now())
        return comment
    }

    fun logi(
        element1: String,
        element2: String = "",
        element3: String = "",
        element4: String = ""
    ) {
        if (element1 != "" && element2 == "" && element3 == "" && element4 == "") {
            Log.d("gg", "${element1}")
        }
        if (element1 != "" && element2 != "" && element3 == "" && element4 == "") {
            Log.d("gg", "${element1} ,${element2}")
        }
        if (element1 != "" && element2 != "" && element3 != "" && element4 == "") {
            Log.d("gg", "${element1} ,${element2} ,${element3}")
        }
        if (element1 != "" && element2 != "" && element3 != "" && element4 != "") {
            Log.d("gg", "${element1} ,${element2} ${element3},${element4}")
        }
    }


}