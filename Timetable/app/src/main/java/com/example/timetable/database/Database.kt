package com.example.timetable.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
//import com.blankj.utilcode.util.ConvertUtils
import com.shiromoku.timetable.tools.DatabaseTools

// TODO: 2021/4/20 check and update code
class Database(context: Context, private val tableName: String) {
    companion object {
        const val databaseName = "timeTable"
        const val version = 1
    }

    private val databaseHelper = DatabaseHelper(context, databaseName, version)


    fun insert(insertObject: Any): Int {
        val jClass = insertObject::class.java
        val values = jClass.declaredFields
        val insertValues = ContentValues()
        for (i in values) {
            i.isAccessible = true
            when (val v = i.get(insertObject)) {
                is Boolean -> insertValues.put(i.name, v)
                is Byte -> insertValues.put(i.name, v)
                is ByteArray -> insertValues.put(i.name, v)
                is Double -> insertValues.put(i.name, v)
                is Float -> insertValues.put(i.name, v)
                is Int -> insertValues.put(i.name, v)
                is Long -> insertValues.put(i.name, v)
                is Short -> insertValues.put(i.name, v)
                is String -> insertValues.put(i.name, v)
            }
        }
        val writer = databaseHelper.writableDatabase
        writer.beginTransaction()
        var flag = 0
        flag = try {
            writer.insertOrThrow(tableName, null, insertValues)
            writer.setTransactionSuccessful()
            1
        } catch (e: SQLiteConstraintException) {
            -1
        } finally {
            writer.endTransaction()
            writer.close()
        }
        return flag
    }

    // TODO: 2021/4/29 有空记得补一下这个 
    // TODO: 2021/4/29 我先把主功能实现吧,要用到再写:p
    fun insert(insertObjectList: MutableList<Any>): Int {
//        val jClass = insertObject::class.java
//        val values = jClass.declaredFields
//        val insertValues = ContentValues()
//        for (i in values) {
//            i.isAccessible = true
//            when (val v = i.get(insertObject)) {
//                is Boolean -> insertValues.put(i.name, v)
//                is Byte -> insertValues.put(i.name, v)
//                is ByteArray -> insertValues.put(i.name, v)
//                is Double -> insertValues.put(i.name, v)
//                is Float -> insertValues.put(i.name, v)
//                is Int -> insertValues.put(i.name, v)
//                is Long -> insertValues.put(i.name, v)
//                is Short -> insertValues.put(i.name, v)
//                is String -> insertValues.put(i.name, v)
//            }
//        }
//        val writer = databaseHelper.writableDatabase
//        writer.beginTransaction()
        var flag = 0
//        flag = try {
//            writer.insertOrThrow(tableName, null, insertValues)
//            writer.setTransactionSuccessful()
//            1
//        } catch (e: SQLiteConstraintException) {
//            -1
//        } finally {
//            writer.endTransaction()
//            writer.close()
//        }
        return flag
    }

    fun delete(deleteOjbect: Any): Int {
        val writer = databaseHelper.writableDatabase
        var columnsName = emptyArray<String>()
        var values = emptyArray<String>()
        val deleteValues = deleteOjbect::class.java.declaredFields
        for (i in deleteValues) {
            i.isAccessible = true
            if (i.get(deleteOjbect) != null) {
                columnsName += i.name
                values += i.get(deleteOjbect).toString()
            }
        }
        val sql = DatabaseTools.generateSQLWith(columnsName, " and ")

        writer.beginTransaction()
        var flag = 0
        flag = try {
            writer.delete(tableName, sql, values)
            writer.setTransactionSuccessful()
            1
        } catch (e: Exception) {
            -1
        } finally {
            writer.endTransaction()
            writer.close()
        }
        return flag
    }

    fun find(findOjbect: Any): MutableList<Any> {
        val reader = databaseHelper.readableDatabase
        val result = mutableListOf<Any>()                   //需要返回的结果
        var columnsName = emptyArray<String>()              //拥有的列名
        var findColumnsName = emptyArray<String>()          //作为查找条件的列名
        var values = emptyArray<String>()                   //查找条件的值

        //反射获取查询数据及返回数据
        val findValues = findOjbect::class.java.declaredFields
        for (i in findValues) {
            i.isAccessible = true
            columnsName += i.name
            if (i.get(findOjbect) != null) {
                findColumnsName += i.name
                values += i.get(findOjbect).toString()
            }
        }
        val sql = DatabaseTools.generateSQLWith(findColumnsName, " and ")

        //执行查询
        val cursor = reader.query(tableName, columnsName, sql, values, null, null, null, null)
        if (cursor.count != 0) {
            cursor.moveToFirst()
            do {
                //遍历构建返回结果
                val singalObject = findOjbect::class.java.newInstance()
                for (i in singalObject::class.java.declaredFields) {
                    i.isAccessible = true
                    //获取数据库记录值
                    //警告: 如果实体类字段有误将会抛出异常
                    val resultValue = when (i.type) {
                        Byte::class.java ->
                            cursor.getBlob(cursor.getColumnIndexOrThrow(i.name))
                        ByteArray::class.java ->
                            cursor.getBlob(cursor.getColumnIndexOrThrow(i.name))
                        Double::class.java ->
                            cursor.getDouble(cursor.getColumnIndexOrThrow(i.name))
                        Float::class.java ->
                            cursor.getFloat(cursor.getColumnIndexOrThrow(i.name))
                        Int::class.java, Integer::class.java ->
                            cursor.getInt(cursor.getColumnIndexOrThrow(i.name))
                        Long::class.java ->
                            cursor.getLong(cursor.getColumnIndexOrThrow(i.name))
                        Short::class.java ->
                            cursor.getShort(cursor.getColumnIndexOrThrow(i.name))
                        String::class.java ->
                            cursor.getString(cursor.getColumnIndexOrThrow(i.name))
                        else -> ""
                    }
                    i.set(singalObject, resultValue)
                }
                //添加至结果
                result.add(singalObject)
            } while (cursor.moveToNext())
        }
        return result
    }

    fun findAll(findOjbect: Any): MutableList<Any> {
        val reader = databaseHelper.readableDatabase
        val result = mutableListOf<Any>()                   //需要返回的结果
        var columnsName = emptyArray<String>()              //拥有的列名
        var findColumnsName = emptyArray<String>()          //作为查找条件的列名
        var values = emptyArray<String>()                   //查找条件的值

        //反射获取查询数据及返回数据
        val findValues = findOjbect::class.java.declaredFields
        for (i in findValues) {
            i.isAccessible = true
            columnsName += i.name
            if (i.get(findOjbect) != null) {
                findColumnsName += i.name
                values += i.get(findOjbect).toString()
            }
        }
        val sql = DatabaseTools.generateSQLWith(findColumnsName, " and ")

        //执行查询
        val cursor = reader.query(tableName, null, null, null, null, null, null, null)
        if (cursor.count != 0) {
            cursor.moveToFirst()
            do {
                //遍历构建返回结果
                val singalObject = findOjbect::class.java.newInstance()
                for (i in singalObject::class.java.declaredFields) {
                    i.isAccessible = true
                    //获取数据库记录值
                    //警告: 如果实体类字段有误将会抛出异常
                    val resultValue = when (i.type) {
                        Byte::class.java ->
                            cursor.getBlob(cursor.getColumnIndexOrThrow(i.name))
                        ByteArray::class.java ->
                            cursor.getBlob(cursor.getColumnIndexOrThrow(i.name))
                        Double::class.java ->
                            cursor.getDouble(cursor.getColumnIndexOrThrow(i.name))
                        Float::class.java ->
                            cursor.getFloat(cursor.getColumnIndexOrThrow(i.name))
                        Int::class.java, Integer::class.java ->
                            cursor.getInt(cursor.getColumnIndexOrThrow(i.name))
                        Long::class.java ->
                            cursor.getLong(cursor.getColumnIndexOrThrow(i.name))
                        Short::class.java ->
                            cursor.getShort(cursor.getColumnIndexOrThrow(i.name))
                        String::class.java ->
                            cursor.getString(cursor.getColumnIndexOrThrow(i.name))
                        else -> ""
                    }
                    i.set(singalObject, resultValue)
                }
                //添加至结果
                result.add(singalObject)
            } while (cursor.moveToNext())
        }
        return result
    }


    // TODO: 2021/4/28 重写此方法
    //可能会推迟到需要用到的那天再重写23333
    fun update(data: ContentValues, where: ContentValues): Int {
        val writer = databaseHelper.writableDatabase

        var columns = emptyArray<String>()
        var values = emptyArray<String>()
//        val newDate = ContentValues()

        for (i in where.keySet()) {
            columns += i
            values += where.getAsString(i)
        }

        val sql = DatabaseTools.generateSQLWith(columns, " and ")

        writer.beginTransaction()
        var flag = 0

        flag = try {
            writer.update(tableName, data, sql, values)
            writer.setTransactionSuccessful()
            1
        } catch (e: Exception) {
            -1
        } finally {
            writer.endTransaction()
            writer.close()
        }

        return flag
    }

    // TODO: 2021/4/29 重写完update后记得来重写这个 :p
//    fun save(data: ContentValues, primaryKey: Array<String>?): Int {
//        var primaryKeyValue = emptyArray<String>()
//
//        val where = ContentValues()
//        if (primaryKey != null) {
//            for (i in data.keySet()) {
//                if (i in primaryKey) {
//                    primaryKeyValue += data.getAsString(i)
//                    where.put(i, data.getAsString(i))
//                }
//            }
//            val existData = find(null, primaryKey, primaryKeyValue)
//            if (existData.isEmpty()) {
//                return insert(data)
//            } else {
//                return update(data, where)
//            }
//        } else {
//            return insert(data)
//        }
//    }

}