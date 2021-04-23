package com.shiromoku.timetable.tools

object DatabaseTools {
    fun generateSQLWith(string: Array<String>, with:String):String{
        var sql = ""
        for (i in string.indices) {
            sql += if (i > 0) with else ""
            sql += string[i]
            sql += "=?"
        }
        return sql
    }
//    fun GenerateSQLWithAnd(string: Array<String>):String{
//        var sql = ""
//        for (i in string.indices) {
//            sql += if (i > 0) " and " else ""
//            sql += string[i]
//            sql += "=?"
//        }
//        return sql
//    }
//    fun GenerateSQLWithcomma(string: Array<String>):String{
//        var sql = ""
//        for (i in string.indices) {
//            sql += if (i > 0) "," else ""
//            sql += string[i]
//            sql += "=?"
//        }
//        return sql
//    }
}