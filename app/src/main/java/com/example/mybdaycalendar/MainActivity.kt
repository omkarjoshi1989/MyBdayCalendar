package com.example.mybdaycalendar

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.Period

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        val listOfPersons = listOf(
            Person("Varada",2020,8,4),
            Person("Rajashree",1991,11,18),
            Person("Omkar",1989,3,24),
        )
        /*listOfPersons.forEach { person->
            person.apply {
                val pastDateTime = LocalDateTime.of(this.year, this.month,this.day, 0, 0, 0)
                val currentDateTime = LocalDateTime.now()
                val period = Period.between(pastDateTime.toLocalDate(), currentDateTime.toLocalDate())
                textView.text = textView.text.toString().plus("\n\n${this.name} is this much old now:\n " +
                        "${period.years} years, ${period.months} months, ${period.days} days")
            }
        }*/

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        personAdapter = PersonAdapter(listOfPersons)
        recyclerView.adapter = personAdapter
    }
}

data class Person(
    var name:String, var year:Int,var month:Int,var day:Int
)
@RequiresApi(Build.VERSION_CODES.O)
class PersonAdapter(private val persons: List<Person>) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    val currentDateTime = LocalDateTime.now()
    var pastDateTime = LocalDateTime.of(1, 1,1, 0, 0, 0)
    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDOB: TextView = itemView.findViewById(R.id.textViewDOB)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val currentPerson = persons[position]
        currentPerson.apply {
            pastDateTime = LocalDateTime.of(this.year, this.month,this.day, 0, 0, 0)
            val period = Period.between(pastDateTime.toLocalDate(), currentDateTime.toLocalDate())
            holder.textViewName.text = currentPerson.name.plus(" (").plus(this.day).plus("/").plus(this.month).plus("/").plus(this.year).plus(")")
                ("${period.years} years, " +
                        "${period.months} months, " +
                        "${period.days} days").also { holder.textViewDOB.text = it }
        }
    }

    override fun getItemCount() = persons.size
}
