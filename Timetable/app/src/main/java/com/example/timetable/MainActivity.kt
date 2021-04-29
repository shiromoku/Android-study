package com.example.timetable

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.timetable.database.Database
import com.example.timetable.entity.Course
import com.example.timetable.entity.CourseInfo
import com.example.timetable.entity.DayCourse
import java.lang.Exception

class MainActivity : Activity() {
    var classNumber = 12
    var itemWidth = 0
    var itemHeight = 0

    var testY = 0

    lateinit var llClassNumber: LinearLayout
    lateinit var ivAdd: ImageView
    lateinit var rlMonday: RelativeLayout
    lateinit var courseDatabase: Database
    lateinit var dayCourseDatabase: Database
    val courseList = mutableListOf<MutableList<CourseInfo>>()
    var dayCourseLayout = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doOnFirstRun()

        initPart()
        setListener()


    }

    private fun doOnFirstRun(){
        val sp = getSharedPreferences("classTime", Context.MODE_PRIVATE)
        if(sp.getBoolean("isFirstRun",true)){
            val spEdit = sp.edit()
            spEdit.putBoolean("isFirstRun",false)
            for(i in 1..12)spEdit.putString("class${i}Time","${i}:00")
            spEdit.commit()
        }

    }

    private fun initPart() {
        llClassNumber = findViewById(R.id.ll_class_number)
        ivAdd = findViewById(R.id.iv_add)
        rlMonday = findViewById(R.id.rl_monday)

        llClassNumber.post {
            itemWidth = llClassNumber.measuredWidth
            itemHeight = llClassNumber.measuredHeight / classNumber

            val sp = getSharedPreferences("classTime", Context.MODE_PRIVATE)
            for (i in 0 until classNumber) {
                val view = View.inflate(this, R.layout.layout_class_number, null)
                val tvClassNumber = view.findViewById<TextView>(R.id.tv_class_number)
                val tvClassTime = view.findViewById<TextView>(R.id.tv_class_time)
                tvClassNumber.text = (i + 1).toString()
                tvClassTime.text = sp.getString("class${i+1}Time","???")
                val params = LinearLayout.LayoutParams(itemWidth, itemHeight)
                view.layoutParams = params
                llClassNumber.addView(view)
            }
        }

        courseDatabase = Database(this, "course")
        dayCourseDatabase = Database(this, "dayCourse")

        dayCourseLayout.add(R.id.rl_monday)
        dayCourseLayout.add(R.id.rl_tuesday)
        dayCourseLayout.add(R.id.rl_wednesday)
        dayCourseLayout.add(R.id.rl_thursday)
        dayCourseLayout.add(R.id.rl_friday)
        dayCourseLayout.add(R.id.rl_saturday)
        dayCourseLayout.add(R.id.rl_sunday)

        val testCourseInfo = CourseInfo(Course("1", "课", "老师"), DayCourse("1", "1", 1, 2, "家"))
        courseList.add(mutableListOf(testCourseInfo))
        val testCourseInfo2 = CourseInfo(Course("1", "课ke", "老师"), DayCourse("1", "1", 1, 1, "家"))
        courseList.add(mutableListOf(testCourseInfo, testCourseInfo2))
        courseList.add(mutableListOf(testCourseInfo2))

    }

    private fun setListener() {
        ivAdd.setOnClickListener {
            updateUi()
        }
    }

    //    inner class UpdateUi : AsyncTaskLoader
    private fun updateUi() {
        if (false && courseList.size != 7) {
            Log.e("On UpdateUi", "updateUi: Incomplete course data")
            Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
            throw Exception()
        }
        for ((counter, dayCourse) in courseList.withIndex()) {
            val layout = findViewById<RelativeLayout>(dayCourseLayout[counter])
            layout.removeAllViews()
            for (course in dayCourse) {
                val courseTime = course.classTo - course.classFrom + 1
                val view = View.inflate(this, R.layout.layout_course, null)
                val layoutParams = RelativeLayout.LayoutParams(itemWidth, itemHeight * courseTime)
                view.setBackgroundColor(randomColor())
                view.y = (itemHeight * (course.classFrom - 1)).toFloat()
                view.layoutParams = layoutParams
                //课程高亮也在这里加啦
                //怎么个高法     加边框咯
//                val cvCourseInfo = view.findViewById<CardView>(R.id.cv_course_info)
//                cvCourseInfo.cardElevation = 300F

                val test = view.findViewById<TextView>(R.id.tv_course_name)
                test.text = course.courseName

                layout.addView(view)
            }
        }
    }

    private fun randomColor(): Int {
        val r = (50..0xCC).random()
        val g = (50..0xCC).random()
        val b = (50..0xCC).random()
        return Color.argb(0xCC, r, g, b)
    }

}