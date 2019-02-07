package edu.gwu.rememberme2

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.io.IOException
import java.util.*
import java.util.Collections.emptyList

class PersistenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveReminder(reminder: Reminder) {

        val reminders = fetchReminders().toMutableList()

        reminders.add(reminder)

        val editor = sharedPreferences.edit()

        //convert a list of scores into a JSON string
        val moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
        val listType = Types.newParameterizedType(List::class.java, Reminder::class.java)
        val jsonAdapter = moshi.adapter<List<Reminder>>(listType)
        val jsonString = jsonAdapter.toJson(reminders)

        editor.putString(Constants.REMINDERS_PREF_KEY, jsonString)

        editor.apply()

    }

    fun fetchReminders(): List<Reminder> {

        val jsonString = sharedPreferences.getString(Constants.REMINDERS_PREF_KEY, null)

        //if null, this means no previous scores, so create an empty array list
        if(jsonString == null) {
            return arrayListOf<Reminder>()
        }
        else {
            //existing reminders, so convert the reminders JSON string into Reminder objects, using Moshi
            val listType = Types.newParameterizedType(List::class.java, Reminder::class.java)
            val moshi = Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()
            val jsonAdapter = moshi.adapter<List<Reminder>>(listType)

            var reminders:List<Reminder>? = emptyList<Reminder>()
            try {
                reminders = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(reminders != null) {
                return reminders.sortedByDescending { it.date }
            }
            else {
                return emptyList<Reminder>()
            }
        }
    }

    fun fetchPhones(): List<String> {

        val jsonString = sharedPreferences.getString(Constants.PHONES_PREF_KEY, null)

        //if null, this means no previous phones, so create an empty array list
        if(jsonString == null) {
            return arrayListOf()
        }
        else {
            //existing phones, so convert the phones JSON string into Phone objects, using Moshi
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val moshi = Moshi.Builder()
                .build()
            val jsonAdapter = moshi.adapter<List<String>>(listType)

            var phones:List<String>? = emptyList<String>()
            try {
                phones = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(phones != null) {
                return phones
            }
            else {
                return emptyList<String>()
            }
        }
    }
}