package com.example.timetable.entity

import com.example.timetable.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

class Course(){
    @PrimaryKey
    var courseId = ""
    var courseName:String? = null
    var courseTeacher:String? = null
    constructor(cId:String,cName:String?,cTheacher:String?):this (){
        courseId = cId
        courseName = cName
        courseTeacher = cTheacher
    }
    constructor(cJson:JSONObject) : this(){
        courseId = cJson.getString("courseId")
        courseName = cJson.getString("courseName")
        courseTeacher = cJson.getString("courseTeacher")
    }

    override fun toString(): String {
        return "Course(courseId='$courseId', courseName=$courseName, courseTeacher=$courseTeacher)"
    }

}
