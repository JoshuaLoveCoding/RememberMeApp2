package edu.gwu.rememberme2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.widget.*
import java.io.Serializable

import java.util.*


class DateTimePickerFragment : AbstractDialogFragment() {

    private var mDate: Date? = null
    private var mDateType: String? = null
    private var mDateOrTimeChoice: String? = null

    /**
     * Maintain a reference to the DatePicker to work around a bug in Android 5.
     * The OnWhatever() listener does not work, so whenver we need to pull the values
     * in the Pickers, we have to reference them directly. We do not, however, need
     * to save these in the Bundle because they can always be recreated if necessary
     * (on device rotation, say).
     */
    private var mDatePicker: DatePicker? = null
    private var mTimePicker: TimePicker? = null

    /**
     * The ResultHandler (if used). Basically, this is so we can use this DialogFragment
     * directly from an Activity (rather than a Fragment).
     */
    private var mResultHandler: ResultHandler? = null

    /**
     * This interface is implemented by the caller, if it wants the result delivered
     * this way. Otherwise, onActivityResult() will be used.
     */
    interface ResultHandler : Serializable {
        fun setDate(result: Date?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Pull the date out of the Fragment Arguments
        processFragmentArguments()
        val calendar = Calendar.getInstance()
        calendar.time = mDate
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        @SuppressLint("InflateParams")
        val view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_time_picker, null)

        // Spinner to choose either Date or Time to edit
        val dateTimeSpinner = view.findViewById(R.id.spinner_date_time_choice) as Spinner
        mDatePicker = view.findViewById(R.id.date_picker)
        mTimePicker = view.findViewById(R.id.time_picker)
        // Note: the OnDateChangedListener does not work in Android 5 when using the
        /// super cool material style of calendar, which is really slick.
        mDatePicker!!.init(year, month, day, null)

        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteOfHour = calendar.get(Calendar.MINUTE)

        mTimePicker!!.currentHour = hourOfDay

        mTimePicker!!.currentMinute = minuteOfHour

        configureDateTimeSpinner(dateTimeSpinner)
        // Now show the Dialog in all its glory!
        return AlertDialog.Builder(getActivity())
            .setView(view)
            .setTitle(R.string.choose_date_or_time)
            .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                val METHOD = "onClick($dialog, $which): "
                if (getTargetFragment() == null && mResultHandler == null) {
                    Log.e(TAG, "Both Target Fragment and ResultHandler are null!")
                } else {

                    mDate = computeDateFromComponents(
                        mDatePicker!!.year,
                        mDatePicker!!.month,
                        mDatePicker!!.dayOfMonth,
                        mTimePicker!!.currentHour,
                        mTimePicker!!.currentMinute
                    )
                    if (mResultHandler == null) {
                        val intent = Intent()
                        intent.putExtra(RESULT_DATE_TIME, mDate)
                        getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            intent
                        )
                    } else {
                        mResultHandler!!.setDate(mDate)
                    }
                }
            })
            .create()
    }

    private fun configureDateTimeSpinner(dateTimeSpinner: Spinner) {
        val choices = ArrayList<String>()
        if (DATE == mDateOrTimeChoice) {
            choices.add(computeChoice(DATE))
        } else if (TIME == mDateOrTimeChoice) {
            choices.add(computeChoice(TIME))
        } else {
            choices.add(computeChoice(TIME))
            choices.add(computeChoice(DATE))
        }
        dateTimeSpinner.adapter = ArrayAdapter(
            getActivity(),
            android.R.layout.simple_list_item_1,
            choices
        )
        dateTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val choice = dateTimeSpinner.adapter.getItem(position) as String
                if (choice.equals(computeChoice(DATE), ignoreCase = true)) {
                    // Make the DatePicker visible
                    mDatePicker!!.visibility = View.VISIBLE
                    mTimePicker!!.visibility = View.GONE
                } else {
                    // Make the TimePicker visible
                    mTimePicker!!.visibility = View.VISIBLE
                    mDatePicker!!.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        // Select initial choice if neither preferred.
        if (mDateOrTimeChoice == null) {
            dateTimeSpinner.setSelection(choices.indexOf(computeChoice(TIME)))
        } else {
            dateTimeSpinner.setSelection(choices.indexOf(computeChoice(mDateOrTimeChoice!!)))
        }
    }

    override fun restoreInstanceState(savedInstanceState: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveInstanceState(outState: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun computeChoice(baseChoice: String): String {
        return "$mDateType $baseChoice"
    }

    override fun processFragmentArguments() {
        mDate = getArguments().getSerializable(FragmentFactory.FRAG_ARG_DATE) as Date?
        // Sanity check
        if (mDate == null) {
            throw RuntimeException("Fragment argument (" + FragmentFactory.FRAG_ARG_DATE + ") cannot be null!")
        }
        mDateType = getArguments().getSerializable(FragmentFactory.FRAG_ARG_DATE_TYPE).toString()
        // Sanity check
        if (mDateType == null || mDateType!!.isEmpty()) {
            throw RuntimeException("Fragment argument (" + FragmentFactory.FRAG_ARG_DATE_TYPE + ") cannot be null!")
        }

        mDateOrTimeChoice = getArguments().getSerializable(FragmentFactory.FRAG_ARG_DATETIME_PICKER_CHOICE).toString()

        mResultHandler = getArguments().getSerializable(FragmentFactory.FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER) as ResultHandler?
    }

    private fun computeDateFromComponents(
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int
    ): Date {
        val METHOD = "computeDateFromComponents($year,$monthOfYear,$dayOfMonth,$hourOfDay,$minuteOfHour)"
        val changedDateCalendar = Calendar.getInstance()
        changedDateCalendar.set(Calendar.YEAR, year)
        changedDateCalendar.set(Calendar.MONTH, monthOfYear)
        changedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        changedDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        changedDateCalendar.set(Calendar.MINUTE, minuteOfHour)
        changedDateCalendar.set(Calendar.SECOND, 0)
        changedDateCalendar.set(Calendar.MILLISECOND, 0)
        val ret = changedDateCalendar.time
        Log.d(TAG, METHOD + "Returning date: " + ret)
        return ret
    }

    companion object {

        val DIALOG_TAG = DateTimePickerFragment::class.java.name

        val RESULT_DATE_TIME = "result." + DateTimePickerFragment::class.java.name
        val TIME = "Time"
        val DATE = "Date"
        val BOTH = "BOTH"

        private val TAG = DateTimePickerFragment::class.java.simpleName
    }
}
