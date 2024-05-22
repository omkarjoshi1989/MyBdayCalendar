package com.example.mybdaycalendar

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.Period

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        textView = findViewById(R.id.textView)
        val listOfPersons = listOf(
            Person("Varada",2020,8,4),
            Person("Rajashree",1991,11,18),
            Person("Omkar",1989,3,24),
        )
        listOfPersons.forEach { person->
            person.apply {
                val pastDateTime = LocalDateTime.of(this.year, this.month,this.day, 0, 0, 0)
                val currentDateTime = LocalDateTime.now()
                val period = Period.between(pastDateTime.toLocalDate(), currentDateTime.toLocalDate())
                textView.text = textView.text.toString().plus("\n\n${this.name} is this much old now:\n " +
                        "${period.years} years, ${period.months} months, ${period.days} days")
            }
        }
    }
}

data class Person(
    var name:String, var year:Int,var month:Int,var day:Int
)