package com.example.timetable.entity

import com.example.timetable.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

class LazyCourseInfo(course: Course, dayCourse: DayCourse){
//    var courseId = ""
    var courseName:String? = null
    var courseTeacher:String? = null

    var day = ""
    var classFrom:Int = 0
    var classTo:Int = 0
    var roomLocation:String? = null
    init {
//        courseId = course.courseId
        courseName =  course.courseName
        courseTeacher =  course.courseTeacher
        day = dayCourse.day
        classFrom = dayCourse.classFrom
        classTo = dayCourse.classTo
        roomLocation = dayCourse.roomLocation
    }
//    constructor(cId:String,cName:String?,cTheacher:String?):this (){
//        courseId = cId
//        courseName = cName
//        courseTeacher = cTheacher
//    }
//    constructor(cJson:JSONObject) : this(){
//        courseId = cJson.getString("courseId")
//        courseName = cJson.getString("courseName")
//        courseTeacher = cJson.getString("courseTeacher")
//    }

}
