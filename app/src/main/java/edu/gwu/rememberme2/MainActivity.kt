package edu.gwu.rememberme2

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.inputmethod.InputMethodManager


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
        recycler_view.adapter = remindersAdapter

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        val menu = navigation.getMenu()
        val menuItem = menu.getItem(0)
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
            if (reminderText != "") {
                val reminder = Reminder(reminderText, Date())
                persistenceManager.saveReminder(reminder)
            }
            editTextReminder.text.clear()
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(editTextReminder.windowToken, 0)
            persistenceManager = PersistenceManager(this)

            val reminders = persistenceManager.fetchReminders()

            remindersAdapter = RemindersAdapter(reminders)

            recycler_view.layoutManager = LinearLayoutManager(this)
            recycler_view.adapter = remindersAdapter
        }
    }
}
