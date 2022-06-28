package com.sg.alma50a

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.alma50a.activities.MainActivity
import com.sg.alma50a.activities_tt.HowToActivity
import com.sg.alma50a.databinding.ActivityHelpBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_POST_NUM
import com.sg.alma50a.utilities.FirestoreClass

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUserID = FirestoreClass().getCurrentUserID()
        if (currentUserID != null) {
            FirestoreClass().getUserDetails(this)
        }

        val pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
     //  pref.edit().putInt(SHARPREF_CURRENT_POST_NUM, 0).apply()
        binding.howToBtn.setOnClickListener {
            startActivity(Intent(this,HowToActivity::class.java))

        }

    }
    fun getingUserData(user: User) {
        currentUser = user
        setText()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun setText() {
        var name = ""
        if (currentUser != null) {
            name = "${currentUser!!.userName} ${currentUser!!.lastName} "
        } else {
            name = "אורח"
        }
       val st="$name"
        binding.tvText1.text = st
        binding.tvText2.text = "הגעת למסך ההסברים"
        val totalString=howToString1()+howToString2()+howToString3()
        binding.tvHowTo.text=totalString
    }
    private fun howToString3(): String{
        val st= "בוא נניח שאתם רוצים להעיר משהו על פוסט כלשהו," +"\n"+
                "פשוט תקליקו על התמונה" +"\n"+
                "והנה הגעתם למסך ההערות," +"\n"+
                "בחלק העליון של המסך ישנם 3 כתפורים" +"\n"+
                "האמצעי מראה את שם המשתמש שלך" +"\n"+
                "אם לא נרשמת את נחשב כ :אורח" +"\n"+
                "הפתור הימני נותן לך את היכולת להכנס אם נרשמת" +"\n"+
                "או להירשם אם אין לך בכלל רשום" +"\n"+
                "הכפתור הימני הקרוי : הגדרות נותן לך את היכולת " +"\n"+
                "לדרג את הפוסט מ 0 לפוסט פח עד 100 פוסט אש " +"\n"+
                "הפתור נותן לך אפשרויות נוספות כמו סדר הופעת הפוסטים " +"\n"+
                "שיכול להיות לפי הדירוג שלך - הפוסטים הטובים בעינך יופיעו ראשונים" +"\n"+
                "ואחריהם בסדר יורד שאר הפוסטים." +"\n"+
                "אתה יכול לסדר את הפוסטים לפי זמן הפירסום שלהם" +"\n"+
                "החדשים יופיעו ראשונים ואחר כך הישנים בסדר יורד." +"\n"+
                "אחרי שלושת הכפתורים מופיע מספר הפוסט" +"\n"+
                "מספר הפוסט חשוב למי שרוצה לחזור לפוסט מסוים," +"\n"+
                "אחרי מספר הפוסט יופיע המלל של הפוסט" +"\n"+
                "כדי שיהי אפשר להתייחס אליו בהערות." +"\n"+
                "ובתחתית המסך מופיע המקום בו אתה מכניס הערות," +"\n"+
                "ובתחתית המסך מופיע המקום בו אתה מכניס הערות," +"\n"+
                "מבחנתי כל הערה מבורכת : על הטקסט הפונטים הצבעים התמונה המלווה הכל" +"\n"+
                "ההערות שהכי עושות לי את זה : איפה המילים האלו פגשו אותך בחיים." +"\n"+
                "נקודה חשובה " +"\n"+
                "מי שלא נרשם ונכנס לא יכול לכתוב הערות ..." +"\n"+
                "מי שחרד לפרטיות שלו שיכניס שם כלשבוא כשם משתמש" +"\n"+
                "ומייל פקטיבי ורק שיהיה בצורה של :  " +"\n"+
                "  x@xx.com"+"\n"
                "------------------------------------------------"
        return st
    }
    private fun howToString2(): String{
        val st= "ו ...  הגעת למסך הראשון אחרי שמסך הפתיחה נעלם" +"\n"+
        "גרור את התמונה מצד ימין לצד שמאל" +"\n"+
                "והנה הגעת לתמונה הבאה," +"\n"+
                "הבה נקרא לתמונה ולמה שכתוב עליה  בשם: פוסט" +"\n"+
                "כל האפליקציה הזאת מורכבת בדי הרבה פוסטים " +"\n"+
                "שכתבתי בשנים האחרונות ועדיין כותב. " +"\n"+
                "האפליקציה עצמה יושבת על הענן" +"\n"+
                " כך שכל מה שאני כותב ומפרסם" +"\n"+
                "מופיע אצל כל מי שיש לו את האפליקציה בזמן אמיתי." +"\n"+
                "  אפשרי שתראו את הפוסט משנה טקסט או צבעים" +"\n"+
                " וזה אומר שאני עובד עליו ברגע ההוא שאתם צופים." +"\n"+
                " נשמע מגניב אבל זה ממש לא מגניב" +"\n"+
                "------------------------------------------------" +"\n"
        return st
    }
    private fun howToString1(): String{
        val st= "השאלה בכלל אם אפשר לקרא לזה אפליקציה ?" +"\n"+
                "לא יודע …" +"\n"+
                "חושב שלא" +"\n"+
                "ובגלל זה נקרא לזה אפליקציה." +"\n"+
                "ובכן" +"\n"+
                "האפליקציה הזאת כולה בנושא מודעות," +"\n"+
                "לא משחקים" +"\n"+
                " לא נקודות" +"\n"+
                " לא לעבור מסכים" +"\n"+
                " לא מנצחים ולא מפסידים," +"\n"+
                " הקיצר זה הזמן לסגור את האפליקציה הזו" +"\n"+
                " ולעבור לאחרת יותר מתגמלת." +"\n"+
                "------------------------------------------------" +"\n"
        return st
    }
}
