package com.shiromoku.timetable.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.shiromoku.timetable.tools.DatabaseTools

// TODO: 2021/4/20 check and update code
class Database(context: Context, val tableName: String) {
    companion object {
        const val databaseName = "goods"
        const val version = 1
    }

    private val databaseHelper = DatabaseHelper(context, databaseName, version)

    fun insert(values: ContentValues): Int {
        val writer = databaseHelper.writableDatabase
        writer.beginTransaction()
        var flag = 0
        flag = try {
            writer.insertOrThrow(tableName, null, values)
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


    fun insert(values: Array<ContentValues>): Int {
        val writer = databaseHelper.writableDatabase
        writer.beginTransaction()
        var flag = 0
        flag = try {
            for (value in values) {
                writer.insertOrThrow(tableName, null, value)
            }
            writer.setTransactionSuccessful()
            1
        } catch (e: java.lang.Exception) {
            -1
        } finally {
            writer.endTransaction()
            writer.close()
        }
        return flag
    }


    //    fun delete(where:ContentValues){
    fun delete(where: MutableMap<String, Any>): Int {
        val writer = databaseHelper.writableDatabase
        var columns = emptyArray<String>()
        var values = emptyArray<String>()
        for (i in where.keys) {
            columns += i
            values += where[i].toString()
        }

        val sql = DatabaseTools.generateSQLWith(columns, " and ")

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


    fun find(
        columns: Array<String>?,
        whereColumns: Array<String>?,
        values: Array<String>?
    ): Array<ContentValues> {
        var returnResult = emptyArray<ContentValues>()
        val reader = databaseHelper.readableDatabase
        var sql: String? = ""
        sql = if (whereColumns != null) {
            DatabaseTools.generateSQLWith(whereColumns, " and ")
        } else {
            null
        }

        val cursor = reader.query(tableName, columns, sql, values, null, null, null, null)
        if (cursor.count == 0) {
            return returnResult
        }
        cursor.moveToFirst()

        do {
            val map = ContentValues()

            for (i in cursor.columnNames) {
                map.put(i,cursor.getString(cursor.getColumnIndex(i)))
//                map[i] = cursor.getString(cursor.getColumnIndex(i))
            }
            returnResult += map

        } while (cursor.moveToNext())
        cursor.close()
        reader.close()
        return returnResult
    }


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


    fun save(data: ContentValues, primaryKey: Array<String>?): Int {
        var primaryKeyValue = emptyArray<String>()

        val where = ContentValues()
        if (primaryKey != null) {
            for (i in data.keySet()) {
                if (i in primaryKey) {
                    primaryKeyValue += data.getAsString(i)
                    where.put(i, data.getAsString(i))
                }
            }
            val existData = find(null, primaryKey, primaryKeyValue)
            if (existData.isEmpty()) {
                return insert(data)
            } else {
                return update(data, where)
            }
        }else{
            return insert(data)
        }
    }

}