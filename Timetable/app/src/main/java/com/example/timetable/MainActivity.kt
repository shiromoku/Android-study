package com.example.timetable

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.timetable.database.Database
import com.example.timetable.entity.Course
import com.example.timetable.entity.DayCourse

class MainActivity : Activity() {
    var classNumber = 12
    var itemWidth = 0
    var itemHeight = 0

    var testY = 100F

    lateinit var llClassNumber: LinearLayout
    lateinit var ivAdd: ImageView
    lateinit var rlMonday: RelativeLayout
    lateinit var courseDatabase: Database
    lateinit var dayCourseDatabase: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPart()
        setListener()

    }

    private fun initPart() {
        llClassNumber = findViewById(R.id.ll_class_number)
        ivAdd = findViewById(R.id.iv_add)
        rlMonday = findViewById(R.id.rl_monday)

        llClassNumber.post {
            itemWidth = llClassNumber.measuredWidth
            itemHeight =  llClassNumber.measuredHeight / classNumber

            for (i in 0 until classNumber) {
                val view = View.inflate(this, R.layout.layout_class_number, null)
                val tvClassNumber = view.findViewById<TextView>(R.id.tv_class_number)
                tvClassNumber.text = (i + 1).toString()
                val params = LinearLayout.LayoutParams(itemWidth,itemHeight)
                view.layoutParams = params
                llClassNumber.addView(view)
            }
        }

        courseDatabase = Database(this,"course")
        dayCourseDatabase = Database(this,"dayCourse")
    }

    private fun setListener(){
        ivAdd.setOnClickListener {
            val view = View.inflate(this,R.layout.layout_course,null)
            view.y = testY
            val test = view.findViewById<TextView>(R.id.tv_course_name)
            test.text = testY.toString()
            test.textSize = 30F
            rlMonday.addView(view)
            testY += 100F

//            courseDatabase.insert(Course("123","456 ","789"))
//            dayCourseDatabase.insert(DayCourse("1","123",1,2,"家里"))
//            dayCourseDatabase.insert(DayCourse("22","1243",1,2,"家里"))
//            dayCourseDatabase.delete(DayCourse("1","123",1,2,"家里"))

//            val result = courseDatabase.find(Course("123",null,null))
            val result = dayCourseDatabase.find(DayCourse("22","1243",null,null,null))
            Log.e("TAG", "setListener: -------------size  ${result.size}", )
            for(i in result) {
                Log.e("TAG", "setListener: $i",)
            }
        }
    }

}