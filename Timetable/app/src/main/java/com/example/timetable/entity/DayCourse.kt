package com.example.timetable.entity

import org.json.JSONObject

class DayCourse() {
    var day = ""
    var courseId = ""
    var classFrom:Int? = null
    var classTo:Int? = null
    var roomLocation:String? = null
    constructor(d:String,cId:String,cFrom:Int?,cTo:Int?,rlocation:String?):this(){
        day = d
        courseId = cId
        classFrom = cFrom
        classTo = cTo
        roomLocation = rlocation
    }
    constructor(DCJson:JSONObject):this(){
        day = DCJson.getString("day")
        courseId = DCJson.getString("courseId")
        classFrom = DCJson.getInt("classFrom")
        classTo = DCJson.getInt("classTo")
        roomLocation = DCJson.getString("roomLocation")
    }

    override fun toString(): String {
        return "DayCourse(day='$day', courseId='$courseId', classFrom=$classFrom, classTo=$classTo, roomLocation=$roomLocation)"
    }

}