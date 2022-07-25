package com.sg.alma50a.activities_tt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.alma50a.R
import com.sg.alma50a.databinding.ActivityHelpExplanationBinding
import com.sg.alma50a.utilities.Constants.HELP_EXPLANATION_INDEX

class HelpExplanationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpExplanationBinding
    private var expalationIndex=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHelpExplanationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        expalationIndex=intent.getIntExtra(HELP_EXPLANATION_INDEX,0)
        when (expalationIndex){
            1-> getText1()
            2-> getText2()
            3-> getText3()
            4-> getText4()
            5-> getText5()
            51-> getText51()
            6-> getText6()
            7-> getText7()
            71-> getText71()
           8-> getText8()
           9-> getText9()
           10-> getText10()

            else-> "אין הסבר לסעיף הזה"
        }
    }

    private fun getText1(){
        val st= "האפליקציה הזאת כוללת חלק מהפוסטים שכתבתי בשנים האחרונות," +"\n"+
                "יש כאן פוסטים לפני שמונה שנים ויש כאן פוסטים של לפני חמש דקות." +"\n"+
                "האפליקציה יושבת על הענן כך שכל מה שאני מפרסם מופיע בזמן אמיתי אצלכם," +"\n"+
                "כן , כמו הפיסבוק והאינסטרגם  רק שפחות יציב ויותר שלי. " +"\n"+
                "  כל הפוסטים בנושאי מודעות מתוך הנסיון האישי שלי בחיים " +"\n"+
                "  אני יודע שזה לא צריך לעניין אף אחד, זה גם לא מיועד לעניין מישהו,  " +"\n"+
                " זה מיועד לתת את התחושה הסוביקטיבית שלי על החיים. " +"\n"+
                " אז אם אתם מחפשים משחק מעניין לעבור מסכים ולצבור נקודות  " +"\n"+
                " זה לא המקום שלכם . " +"\n"+
                " אם התחושה שלכם כמו שלי שהעולם הזה לפעמים קטן מידי ומחניק,  " +"\n"+
                " אז יש סיכוי מה שתמצאו משהו כאן שידבר אליכם . " +"\n"
             binding.tvExplenation.text=st
    }
    private fun getText2(){
        val st= "אז איך מתחילים," +"\n"+
         "פשוט החלק את האצבע על התמונה משמאל לימין והנה הגעת לפוסט הבא," +"\n"+
         "במידה והחלקת חזק מידי מספר פוסטים ירוצו עד שיעצר לפי עוצמת ההחלקה," +"\n"+
         "זה יכול להיות משחק יפה להחליק כמה תמונות במכה למי שאוהב משחקי החלקה." +"\n"+
         "במידה ותקליק על התמונה תגיעה למסך ההערות," +"\n"+
         "לכל פוסט יש מסך הערות משלו, שם אתה יכול לכתוב את ההשגות שלך על הפוסט " +"\n"+
                "ואפילו לפתח דיון על הפוסט עם שאר המשתתפים " +"\n"+
                "ראה פירוט בלחצן העזרה של --מסך ההערות--"
        binding.tvExplenation.text=st
    }
    private fun getText3(){
        val st= "הפוסטים מופיעים כבררת מחדל לפי זמן הפירסום שלהם," +"\n"+
                "אם פרסמתי פוסט א' לפני חמש דקות ופוסט ב' לפני עשר דקות" +"\n"+
                "   אז פוסט א' יופיע לפני פוסט ב',  " +"\n"+
                " כלומר זמן פירסום הפוסט קובע את מיקומו באפליקציה.  " +"\n"+
                "האפליקציה נעזרת בענן כך שברגע שפוסט מפורסם או שמישהו מעיר הערה" +"\n"+
                "זה מופיע אצלכם מיד במכשיר." +"\n"+
                "כללית זמן הפרסום של הפוסט אינו שלב היצירה שלו" +"\n"+
                "כלומר אני יכול לפרסם פוסט שנכתב לפני שנים" +"\n"+
                "ואחריו פוסט שנכתב לפני דקה." +"\n"+
                "והם יופיעו אחד אחרי השני." +"\n"
        binding.tvExplenation.text=st
    }
    private fun getText4(){
        val st= "לכל פוסט אתה יכול להיכנס ולתת דרוג אישי מאפס עד מאה," +"\n"+
                "אפס  -> לפוסט פח שממש לא עשה לך את זה " +"\n"+
                "ומאה -> לפוסט אש שממש התחברת אליו." +"\n"+
                "איך נותנים ציון לפוסט ?" +"\n"+
                "ובכן, הגעת לפוסט מסוים שאתה רוצה לדרג אותו" +"\n"+
                "הקלק על התמונה של הפוסט " +"\n"+
                " --הגדרות--" +"\n"+
                " --דרג את הפוסט הזה--" +"\n"+
                " הכנס את הדירוג שלך " +"\n"+
                " --לחץ כדי להמשיך--." +"\n"+
                " אפשר לשנות את הדירוג הזה בכל שלב ." +"\n"
        binding.tvExplenation.text=st
    }
    private fun getText5(){
        val st= "צורה נוספת של הצגת הפוסטים" +"\n"+
                "והיא הצגה של כל הפוסטים לפי הדרוג האישי שנתת להם" +"\n"+
                "פוסטים עם הדירוג הגבוה יופיעו ראשונים " +"\n"+
                "לדוגמה פוסט שקיבל 88 יופיע לפני פוסט שקיבל 87, " +"\n"+
                "לפיכך רצוי שתחשוב על הדירוג שאתה נותן לכל פוסט. " +"\n"+
                "אף אחד לא מכיר ולא יודע את הדירוגים האלה שלך, הוא אישי ונשמר אצלך במכשיר." +"\n"+
                "עכשיו לתהליך ההצגה של הפוסטים לפי הדירוג  האישי שלך: " +"\n"+
                "הקלק על תמונה כלשהי" +"\n"+
                " --הגדרות--" +"\n"+
                "--סדר את הפוסטים לפי הדירוגים שלך--" +"\n"+
                "והנה קבלת את סדר הפוסטים שאתה רוצה" +"\n"+
                "בשבל מה זה טוב ?" +"\n"+
                "יש מאות פוסטים ולא תמיד יש לך חשק לעבור עליהם אחד לאחד," +"\n"+
                "בא לך לעבור על הפוסטים המדורגים שלך תחילה , ואחר כך על השאר," +"\n"+
                "אז הנה יש לך את האפשרות הזו." +"\n"
        binding.tvExplenation.text=st
    }
    private fun getText51(){
        val st= "צורה נוספת של הצגה" +"\n"+
                "היא לפי הפוסטים המומלצים," +"\n"+
                " המומלצים אלו פוסטים שקיבלו את הדירוג הכי גבוה על ידי המשתמשים " +"\n"+
                "  אפשר שהם יותר פשוטים להבנה, או נוגעים ביותר אנשים או פשוט מוצלחים יותר ... " +"\n"
                "  גם אתם יכולים להמליץ על פוסט מסוים ב --הערות - כללי -- " +"\n"
        binding.tvExplenation.text=st
    }
    private fun getText6(){
        val st=  "צורת עבודה מומלצת: " +"\n"+
            "תתחיל תמיד עם סידור פוסטים לפי זמן פירסום כדי לראות מה חדש," +"\n"+
               "במידה ונתקלת בפוסט שעשה לך את זה, דרג אותו." +"\n"+
                "עבור למומלצים ובחר משם פוסטים " +"\n"+
                "(דירוג המחדל של כל פוסט הוא אפס)" +"\n"+
                "אחרי שגמרת לחרוש מה חדש ומה מומלץ עבור לראות את הפוסטים לפי הדירוג  האישי שלך." +"\n"
        binding.tvExplenation.text=st
    }
    private fun getText7(){
        val st=  "כדי להגיע למסך ההערות פשוט הקלק על התמונה בפוסט " +"\n"+
                "והנה הגעת למסך ההערות , נכון זה לא היה קשה ..." +"\n"+
                "בחלק העליון של המסך שלשה כפתורים :" +"\n"+
                "הכפתור הימני:  --הכנס--   כדי להיכנס לחשבון האישי שלך, " +"\n"+
                "הכפתור האמצעי:  נותן את שם המשתמש שלך," +"\n"+
                "הכפתור השמאלי:  --הגדרות--  מעביר אותך למסך ההגדרות. " +"\n"+
                "מתחת לשורת הכפתורים מופיע מספר הפוסט ואחר כך פירוט הפוסט. " +"\n"+
                "בתחתית המסך יש מקום בתוך התחום הירוק לכתוב את הערה שלך" +"\n"+
                "וחץ מצד שמאל -->  כדי לשלוח את הערה לענן, והיא תופיע אצלך ואצל כל המשתמשים בזמן אמיתי." +"\n"+
                "על מה כותבים הערות ?" +"\n"+
                "על צורה: תמונה , פונט, צבעים, העמדה... " +"\n"+
                "על מהות: מה אני מרגיש וחושב על מה שנכתב בפוסט ועל מה שאחרים כתבו," +"\n"+
                "אישית הייתי רוצה לדעת איפה המילים האלה פגשו אותכם. " +"\n"+
                "נקודה חשובה " +"\n"+
                "רק מי שנרשם (ראה בסעיף הבא --רישום--) יכול לכתוב הערות, " +"\n"+
                "וההגיון מאחורי זה : אם אין לך שם משתמש אי אפשר להתייחס למה שאתה כותב." +"\n"+
                "כדי לערוך הערה שכתבת או לבטל אותה , לחץ על איזור הערה ותועבר למסך שמאפשר זאת." +"\n"+
                ""
        binding.tvExplenation.text=st
    }
    private fun getText71(){
        val st=  "הגעת למסך ההסבר על ההערות-  כללי" +"\n"+
                "המסך הזה כשמו כן הוא" +"\n"+
                "לכתיבת הערות כלליות על האפליקציה " +"\n"+
                "צורה תיפעול תוכן וכו' ..." +"\n"+
                "במדה ותרצה שפוסט מסוים יופיע במומלצים " +"\n"+
                "זה המקום להמליץ עליו. " +"\n"+
                ""
        binding.tvExplenation.text=st
    }
    private fun getText8(){
        val st=  " הגעת למסך העזרה ברישום, " +"\n"+
          " ועל ההתחלה נבהיר אין ברישום שום פגיעה בפרטיות שלך... " +"\n"+
          " אתה יכול להכניס שם משתמש וכתובת מייל פקטיבית כך שאיש (כולל אני) לא יזהו אותך.  " +"\n"+
          " אם נרשמת ונכנסת למכשיר שלך בפעם הבאה האפליקציה תכניס אותך מידית. " +"\n"+
          " במידה ואתה רשום כבר במסך הערות בכפתור האמצעי יופיע שם המשתמש שלך," +"\n"+
          " במידה שלא יופיע שם : --אורח--." +"\n"+
          "ובמידה ואתה בחזקת אורח ואתה רוצה להרשם ...." +"\n"+
          "הקלד על  --הכנס--" +"\n"+
          "-- אם אין לך חשבון לחץ כאן --" +"\n"+
          "מלא את כל מה שנדרש" +"\n"+
                "--לחץ כדי לבצע רישום --" +"\n"+
                "הרישום הזה הוא  שלב ראשון של הרישום." +"\n"+
                "הוא לכשעצמו מספיק אבל ממולץ לבצע גם את השלב השני של הרישום." +"\n"+
                "-------------------------------------" +"\n"+
                "השלב השני של הרישום :" +"\n"+
                "(שהוא חופף לשינוי הפרופיל)" +"\n"+
                "הקלד על תמונה כלשהיא" +"\n"+
                "--הגדרות--" +"\n"+
                "--שנה פרופיל--" +"\n"+
                "הכנס תמונה מזהה משלך על ידי הקלקה על התמונה הקיימת" +"\n"+
                "הכנס זכר / נקבה" +"\n"+
                "--לחץ כדי לשמור שינויים בפרופיל--" +"\n"+
                ""
        binding.tvExplenation.text=st
    }
    private fun getText9(){
        val st=  " לחץ על תמונה כלשהיא " +"\n"+
              " --הגדרות- " +"\n"+
              " הגעת למסך שינוי פרופיל " +"\n"+
              "כמו תמיד לחץ על איזשהיא תמונה" +"\n"+
              "--הגדרות--" +"\n"+
              "  --שנה פרופיל--" +"\n"+
              "נכנסת לפרופיל הקיים שלך כרגע " +"\n"+
              "לחץ על התמונה בראש העמוד ועדכן אותה למשהו משלך" +"\n"+
              "עדכן את שאר הפרטים " +"\n"+
                "הכנס זכר / נקבה" +"\n"+
              "-- שמור שינויים בפרופיל--" +"\n"+
                ""
        binding.tvExplenation.text=st
    }
    private fun getText10(){
        val st=  " לחץ על תמונה כלשהיא " +"\n"+
                " --הגדרות- " +"\n"+
                "הגעת למסך ההגדרות הבה נעבור על המסך סעיף סעיף," +"\n"+
                "דרג את הפוסט הזה --> תן את הדירוג האישי שלך לפוסט שאתה נמצא עליו כרגע." +"\n"+
                "" +"\n"+
                "סדר את הפוסטים לפי הדירוגים שלך--> דובר על זה,  זה מובן" +"\n"+
                "" +"\n"+
                "סדר את הפוסטים לפי תאריך פרסום  --> דובר על זה,  זה מובן" +"\n"+
                "" +"\n"+
                "שנה פרופיל -->   ראה סעיף קודם של שנה פרופיל." +"\n"+
                "עבור לפוסט אחר --> מעבר לפוסט אחר מהמקום שאתה נמצא כרגע." +"\n"+
                "מסך הערות --> מעביר אותך למסך עם כל ההערות באפליקציה לפי תאריך הכתיבה שלהם." +"\n"+
                " אם כתבת הערה כלשהיא ואתה רוצה לדעת אם יש התייחסות," +"\n"+
                " אתר את הערה לפי התאריך שהיא נכתבה," +"\n"+
                " לחץ על ההערה ואתה תועבר לפוסט המדובר." +"\n"+
                "מסך פתיחה --> אפשרות לקבוע אחרי כמה זמן המסך פתיחה יעלם, במידה ותכניס אפס שניות המסך פתיחה לא יופיע ותגיע ישר לפוסטים. " +"\n"+
                "מצב אורח --> אפשרות לגלוש שלא בשם שלך , אלא כאורח (וזה אומר שלא תוכל לכתוב הערות). " +"\n"+
                ""
        binding.tvExplenation.text=st
    }
}
