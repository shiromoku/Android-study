package com.example.timetable

import android.util.Log
import com.example.timetable.entity.Course
import com.example.timetable.entity.DayCourse
import org.junit.Test
import kotlin.math.log

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
        val c = Course("12","23",null)
        val ck = c::class.java
        val values = ck.declaredFields


        for(i in values){
            i.isAccessible = true
            val x = i.get(c)
            var str = "---------${i.name}----${i.type}----$x--------"
            i.set(c,"555")
            println(str)
        }
        print("\n\n\n")
        for(i in values){
            i.isAccessible = true
            val x = i.get(c)
            var str = "---------${i.name}--------$x--------"
//            i.set(c,"555")
            println(str)
        }
    }

    @Test
    fun test(){
//        val dc = DayCourse("1","1",3,4,"1")
        val c = Course("12","23",null)
        val ck = c::class.java
        for(i in ck.declaredFields) {
            println("----${i.name}---${i.annotations.size}")
            i.annotations.forEach {

                if(it is PrimaryKey) {
                    println("${i.name}--yes")
                }
                else {
                    println("${i.name}--no")
                }
            }

        }
    }

    @Test
    fun colorTest(){
        val color = 0xFFFF0000.toInt()
        println(color)
    }

    private fun randomColor():Int{
        var result = 0xFF
        result += (0x55..0xCC).random()
        result *= 100
        result += (0x55..0xCC).random()
        result *= 100
        result += (0x55..0xCC).random()
        return result.toInt()
    }
}