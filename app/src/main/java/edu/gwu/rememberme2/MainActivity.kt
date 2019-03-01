package edu.gwu.rememberme2

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.inputmethod.InputMethodManager
import android.widget.SimpleAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var persistenceManager: PersistenceManager
    private lateinit var remindersAdapter: RemindersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        persistenceManager = PersistenceManager(this)

        val reminders = persistenceManager.fetchReminders()

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

    fun confirmButtonPressed(view: View) {
        button.setOnClickListener {
            val reminderText = editTextReminder.text.toString()
            if (reminderText != "") { // length not equals to 0
                val reminder = Reminder(reminderText, Date())
                persistenceManager.saveReminder(reminder)
            }
            editTextReminder.text.clear()
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(editTextReminder.windowToken, 0) //hide keyboard
            persistenceManager = PersistenceManager(this)

            val reminders = persistenceManager.fetchReminders()

            remindersAdapter = RemindersAdapter(reminders)

            recycler_view.layoutManager = LinearLayoutManager(this)
            recycler_view.adapter = remindersAdapter
        }
    }
}
