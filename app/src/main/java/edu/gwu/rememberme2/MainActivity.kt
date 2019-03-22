package edu.gwu.rememberme2

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker


class MainActivity : AppCompatActivity() {
    private lateinit var persistenceManager: PersistenceManager //set persistence parameter
    private lateinit var remindersAdapter: RemindersAdapter
    internal lateinit var DateEdit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        persistenceManager = PersistenceManager(this)

        val reminders = persistenceManager.fetchReminders() //fetch reminders

        remindersAdapter = RemindersAdapter(reminders)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = remindersAdapter //recycler view show items

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recycler_view.adapter as RemindersAdapter
                adapter.removeAt(viewHolder.adapterPosition) //delete reminder
                persistenceManager.saveReminders(reminders) //save new reminders
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        val menu = navigation.getMenu()
        val menuItem = menu.getItem(0) //set index
        menuItem.setChecked(true) //set the clicked button color
        navigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        val a = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(a)
                        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)//show from left side
                        return true // change listener state
                    }
                    R.id.navigation_notifications -> {
                        val b = Intent(this@MainActivity, AlertActivity::class.java)
                        startActivity(b)
                        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)//show from right side
                        return true // change listener state
                    }
                }
                return false
            }
        })
    }

    fun setButtonPressed(view: View) {
        button2.setOnClickListener {
            showTruitonTimePickerDialog(view)
            showTruitonDatePickerDialog(view)
        }
    }

    fun confirmButtonPressed(view: View) {
        button.setOnClickListener {
            val reminderText = editTextReminder.text.toString()//get text
            if (reminderText != "") { // length not equals to 0
                val reminder = Reminder(reminderText, Date())
                persistenceManager.saveReminder(reminder)//keep new reminder
            }
            editTextReminder.text.clear()
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(editTextReminder.windowToken, 0) //hide keyboard
            persistenceManager = PersistenceManager(this)

            val reminders = persistenceManager.fetchReminders()

            remindersAdapter = RemindersAdapter(reminders)

            recycler_view.layoutManager = LinearLayoutManager(this)//reload the view
            recycler_view.adapter = remindersAdapter
        }
    }

    fun showTruitonDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(activity!!, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
            //DateEdit.setText(day.toString() + "/" + (month + 1) + "/" + year)
        }
    }

    fun showTruitonTimePickerDialog(v: View) {
        val newFragment = TimePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(
                activity, this, hour, minute,
                DateFormat.is24HourFormat(activity)
            )
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            // Do something with the time chosen by the user
            //DateEdit.setText(DateEdit.getText() + " -" + hourOfDay + ":" + minute)
        }
    }
}
