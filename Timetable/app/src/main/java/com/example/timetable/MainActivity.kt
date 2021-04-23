package com.example.timetable

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    var classNumber = 12
    var itemWidth = 0
    var itemHeight = 0

    lateinit var llClassNumber: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPart()
//         TODO: 2021/4/23
              readAndApplySetting()     //like how many class a day     the class are what

        for (i in 0 until classNumber) {
            val view = View.inflate(this, R.layout.layout_class_number, null)
            val tvClassNumber = view.findViewById<TextView>(R.id.tv_class_number)
            tvClassNumber.text = (i + 1).toString()
            val params = LinearLayout.LayoutParams(itemWidth,itemHeight)
            view.layoutParams = params
            llClassNumber.addView(view)
        }


    }

    fun initPart() {
        llClassNumber = findViewById(R.id.ll_class_number)
    }

    fun readAndApplySetting(){

//        val vto = llClassNumber.viewTreeObserver
//        vto.addOnPreDrawListener {
//            itemWidth = llClassNumber.measuredWidth
//            itemHeight =  llClassNumber.measuredHeight / classNumber
//
//            for (i in 0 until classNumber) {
//                val view = View.inflate(this, R.layout.layout_class_number, null)
//                val tvClassNumber = view.findViewById<TextView>(R.id.tv_class_number)
//                tvClassNumber.text = (i + 1).toString()
//                val params = LinearLayout.LayoutParams(itemWidth,itemHeight)
//                view.layoutParams = params
//                llClassNumber.addView(view)
//            }
//        Log.e("TAG", "readAndApplySetting: $itemWidth $itemHeight" )
//            true
//        }
        itemHeight = 180
        itemWidth = 110
    }

}