package com.example.timetable

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.timetable.database.Database
import com.example.timetable.entity.Course
import com.example.timetable.entity.DayCourse
import com.example.timetable.entity.LazyCourseInfo
import com.example.timetable.viewHolder.CourseHolder
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception

class MainActivity : Activity() {
    var classNumber = 12
    var itemWidth = 0
    var itemHeight = 0

    lateinit var llClassNumber: LinearLayout
    lateinit var ivAdd: ImageView
    lateinit var iAddClass: View
    lateinit var rlMonday: RelativeLayout
    lateinit var vTopClassDetail: RelativeLayout

    lateinit var etCourseId: TextInputEditText
    lateinit var etCourseName: TextInputEditText
    lateinit var etCourseDay: TextInputEditText
    lateinit var etCourseTimeStart: TextInputEditText
    lateinit var etCourseTimeEnd: TextInputEditText

    lateinit var btnAddConfirm: Button
    lateinit var btnAddCancel: Button

    lateinit var courseDatabase: Database
    lateinit var dayCourseDatabase: Database
    lateinit var lazyCourseInfoDatabase: Database

    val loadingData = LoadingDataFromDatabase()

    //    val courseList = mutableListOf<MutableList<CourseInfo>>()
    val courseList = mutableListOf<MutableList<LazyCourseInfo>>()
    var dayCourseLayout = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doOnFirstRun()

        initPart()
        setListener()

        vTopClassDetail.removeAllViewsInLayout()


    }

    private fun doOnFirstRun() {
        val sp = getSharedPreferences("classTime", Context.MODE_PRIVATE)
        if (sp.getBoolean("isFirstRun", true)) {
            val spEdit = sp.edit()
            spEdit.putBoolean("isFirstRun", false)
            for (i in 1..12) spEdit.putString("class${i}Time", "${i}:00")
            spEdit.apply()
        }

    }

    private fun initPart() {
        llClassNumber = findViewById(R.id.ll_class_number)
        ivAdd = findViewById(R.id.iv_add)
        rlMonday = findViewById(R.id.rl_monday)
        iAddClass = findViewById(R.id.i_add_class)
        vTopClassDetail = findViewById(R.id.v_top_class_detail)

        etCourseId = findViewById(R.id.et_course_id)
        etCourseName = findViewById(R.id.et_course_name)
        etCourseDay = findViewById(R.id.et_course_day)
        etCourseTimeStart = findViewById(R.id.et_course_time_start)
        etCourseTimeEnd = findViewById(R.id.et_course_time_end)

        btnAddConfirm = findViewById(R.id.btn_add_confirm)
        btnAddCancel = findViewById(R.id.btn_add_cancel)

        llClassNumber.post {
            itemWidth = llClassNumber.measuredWidth
            itemHeight = llClassNumber.measuredHeight / classNumber
            //读取上课时间,假的,都是假的
            val sp = getSharedPreferences("classTime", Context.MODE_PRIVATE)
            for (i in 0 until classNumber) {
                val view = View.inflate(this, R.layout.layout_class_number, null)
                val tvClassNumber = view.findViewById<TextView>(R.id.tv_class_number)
                val tvClassTime = view.findViewById<TextView>(R.id.tv_class_time)
                tvClassNumber.text = (i + 1).toString()
                tvClassTime.text = sp.getString("class${i + 1}Time", "???")
                val params = LinearLayout.LayoutParams(itemWidth, itemHeight)
                view.layoutParams = params
                llClassNumber.addView(view)
            }
            loadingData.execute(null)
        }

        courseDatabase = Database(this, "course")
        dayCourseDatabase = Database(this, "dayCourse")
        lazyCourseInfoDatabase = Database(this, "lazyCourseInfo")

        dayCourseLayout.add(R.id.rl_monday)
        dayCourseLayout.add(R.id.rl_tuesday)
        dayCourseLayout.add(R.id.rl_wednesday)
        dayCourseLayout.add(R.id.rl_thursday)
        dayCourseLayout.add(R.id.rl_friday)
        dayCourseLayout.add(R.id.rl_saturday)
        dayCourseLayout.add(R.id.rl_sunday)

//        iAddClass.re
    }

    private fun setListener() {
        btnAddCancel.setOnClickListener {
            etCourseId.text = null
            etCourseName.text = null
            etCourseDay.text = null
            etCourseTimeStart.text = null
            etCourseTimeEnd.text = null
            iAddClass.visibility = View.GONE
        }

        btnAddConfirm.setOnClickListener {
            val cId = etCourseId.text.toString()
            val cName = etCourseName.text.toString()
            val day = etCourseDay.text.toString()
            val from = etCourseTimeStart.text.toString().toInt()
            val to = etCourseTimeEnd.text.toString().toInt()
            val dayCourse = DayCourse(day, cId, from, to, null)
            val course = Course(cId, cName, null)

            val lazyCourseInfo = LazyCourseInfo(course, dayCourse)

            Toast.makeText(this, "正在保存", Toast.LENGTH_SHORT).show()
            btnAddConfirm.isEnabled = false
            Thread {
                val result = lazyCourseInfoDatabase.insert(lazyCourseInfo)
                if (result == 1) {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
                    btnAddCancel.performClick()
                    when (day) {
                        "1" -> courseList[0].add(lazyCourseInfo)
                        "2" -> courseList[1].add(lazyCourseInfo)
                        "3" -> courseList[2].add(lazyCourseInfo)
                        "4" -> courseList[3].add(lazyCourseInfo)
                        "5" -> courseList[4].add(lazyCourseInfo)
                        "6" -> courseList[5].add(lazyCourseInfo)
                        "7" -> courseList[6].add(lazyCourseInfo)
                    }
                    runOnUiThread { updateUi() }
                } else
                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
            }.run()
//            val service = this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE)

        }

        ivAdd.setOnClickListener {
            iAddClass.visibility = View.VISIBLE
        }
    }

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
                // TODO: 2021/5/19 淦啦,我不想写这个功能了
//                val cvCourseInfo = view.findViewById<CardView>(R.id.cv_course_info)
//                cvCourseInfo.cardElevation = 300F

                val text = view.findViewById<TextView>(R.id.tv_course_name)
                text.text = course.courseName
                view.setOnTouchListener(ClassOnTouchListener())

                val holder = CourseHolder(text)
                view.tag = holder

                layout.addView(view)
            }
        }
    }

    inner class ClassOnTouchListener() : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            val holder = v?.tag as CourseHolder
            Log.e("TAG", "onTouch: ++++++++++++++++++++${holder.tvCourseName.text}+++++++++++++++++++", )
            var flag = true
            event?.let {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.e("TAG", "onTouch: -------Down----------${v?.x}-----${v?.y}---------${v?.width}----${v?.height}------------", )

                        flag = true
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.e("TAG", "onTouch: -------Up----------${v?.x}-----${v?.y}---------${v?.width}----${v?.height}-------", )
                        flag = false
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        Log.e("TAG", "onTouch: -------Cancel----------${v?.x}-----${v?.y}---------${v?.width}----${v?.height}------------", )

                        flag = true
                    }
                    MotionEvent.ACTION_MOVE -> {
//                        Log.e("TAG", "onTouch: -------Move----------${v?.x}-----${v?.y}---------${v?.width}----${v?.height}------------", )

                        flag = true
                    }
                    else -> {
                        Log.e("TAG", "onTouch: ------Else-----------${it.action}----------------", )
                        flag = true
                    }
                }
            }
            return flag
        }
    }

    fun showOrHideClassCard(holder:CourseHolder,v:View){

    }

    private fun randomColor(): Int {
        val r = (50..0xCC).random()
        val g = (50..0xCC).random()
        val b = (50..0xCC).random()
        return Color.argb(0xCC, r, g, b)
    }

    //以后学了再用,现在滚粗啦
//    inner class LoadingDataFromDatebase : AsyncTaskLoader
    inner class LoadingDataFromDatabase : AsyncTask<Int?, Int?, Int?>() {
        val LOADING_DATA_SUCCESS = 0x010
        override fun doInBackground(vararg params: Int?): Int? {
            courseList.clear()

            val mondayList = mutableListOf<LazyCourseInfo>()
            val tuesdayList = mutableListOf<LazyCourseInfo>()
            val wednesdayList = mutableListOf<LazyCourseInfo>()
            val thursdayList = mutableListOf<LazyCourseInfo>()
            val fridayList = mutableListOf<LazyCourseInfo>()
            val saturdayList = mutableListOf<LazyCourseInfo>()
            val sundayList = mutableListOf<LazyCourseInfo>()
            val data = lazyCourseInfoDatabase.findAll(LazyCourseInfo(Course(), DayCourse()))

            for (i in data) {
                val a: LazyCourseInfo = i as LazyCourseInfo
                when (a.day) {
                    "1" -> mondayList.add(a)
                    "2" -> tuesdayList.add(a)
                    "3" -> wednesdayList.add(a)
                    "4" -> thursdayList.add(a)
                    "5" -> fridayList.add(a)
                    "6" -> saturdayList.add(a)
                    "7" -> sundayList.add(a)
                    else -> {
                        Log.e("MainActivity", "doInBackground: day data out of range")
                        Toast.makeText(this@MainActivity, "error was happend", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            courseList.add(mondayList)
            courseList.add(tuesdayList)
            courseList.add(wednesdayList)
            courseList.add(thursdayList)
            courseList.add(fridayList)
            courseList.add(saturdayList)
            courseList.add(sundayList)
            return LOADING_DATA_SUCCESS
        }

        override fun onPostExecute(result: Int?) {
            when (result) {
                LOADING_DATA_SUCCESS -> {
                    runOnUiThread {
                        updateUi()
                    }
                }
//                runOnUiThread(Runnable().apply { updateUi()
            }
        }
    }
}