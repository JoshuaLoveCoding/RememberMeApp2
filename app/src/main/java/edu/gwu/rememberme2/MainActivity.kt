package edu.gwu.rememberme2

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
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
