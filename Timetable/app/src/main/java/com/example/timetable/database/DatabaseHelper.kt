package com.example.timetable.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// TODO: 2021/4/20 check and update code
open class DatabaseHelper(context: Context, databaseName:String, version: Int) : SQLiteOpenHelper(context,databaseName,null,version) {
    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "create table if not EXISTS course(" +
                "courseId text not null  primary key ," +
                "courseName text ," +
                "courseTeacher text " +
                ")"
        db!!.execSQL(sql)

        sql =  "create table if not EXISTS dayCourse(" +
                "day text not null," +
                "courseId text not null," +
                "classFrom int," +
                "classTo int," +
                "roomLocation text" +
                ")"
        db.execSQL(sql)

        sql =  "create table if not EXISTS lazyCourseInfo(" +
                "courseName text ," +
                "courseTeacher text " +
                "day text not null," +
                "classFrom int," +
                "classTo int," +
                "roomLocation text" +
                ")"
        db.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}