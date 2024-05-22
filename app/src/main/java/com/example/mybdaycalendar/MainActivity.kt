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
import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter
    private val listOfPersons = mutableListOf<Person>()
    private lateinit var buttonAdd: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonAdd.setOnClickListener {
            val addPersonDialog = AddPersonDialog(this) { person ->
                listOfPersons.add(Person(person.name, person.year, person.month, person.day))
                personAdapter.notifyDataSetChanged()
            }
            addPersonDialog.show()
            personAdapter = PersonAdapter(listOfPersons)
            recyclerView.adapter = personAdapter
        }
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

class AddPersonDialog(context: Context, private val listener: (Person) -> Unit) {

    private val dialog: AlertDialog = AlertDialog.Builder(context)
        .setTitle("Add Person")
        .setView(R.layout.dialog_add_person)
        .setPositiveButton("Add") { _, _ -> }
        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        .create()

    init {
        dialog.setOnShowListener {
            val nameEditText = dialog.findViewById<EditText>(R.id.editTextName)
            val yearEditText = dialog.findViewById<EditText>(R.id.editTextYear)
            val monthEditText = dialog.findViewById<EditText>(R.id.editTextMonth)
            val dayEditText = dialog.findViewById<EditText>(R.id.editTextDay)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = nameEditText?.text.toString()
                val year = yearEditText?.text.toString().toIntOrNull()
                val month = monthEditText?.text.toString().toIntOrNull()
                val day = dayEditText?.text.toString().toIntOrNull()

                if (name.isNotEmpty() && year != null && month != null && day != null) {
                    listener.invoke(Person(name, year, month, day))
                    dialog.dismiss()
                } else {
                    // Handle invalid input here (e.g., show an error message)
                }
            }
        }
    }

    fun show() {
        dialog.show()
    }
}
