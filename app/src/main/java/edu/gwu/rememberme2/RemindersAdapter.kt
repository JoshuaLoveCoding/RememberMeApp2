package edu.gwu.rememberme2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RemindersAdapter(private val reminders: List<Reminder>):
    RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup?.context)

        return ViewHolder(layoutInflater.inflate(R.layout.row_reminder, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val reminder = reminders.get(position)

        viewHolder.bind(reminder)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val reminderTextView: TextView = view.findViewById(R.id.reminder_textview)
        private val dateTextView: TextView = view.findViewById(R.id.date_textview)

        fun bind(reminder: Reminder) {
            reminderTextView.text = reminder.reminder
            dateTextView.text = reminder.date.toString()
        }
    }
}