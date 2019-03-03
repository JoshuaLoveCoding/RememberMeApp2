package edu.gwu.rememberme2

import android.os.Bundle
import java.util.*

object FragmentFactory {
    val FRAG_ARG_PREFIX = "fragment.argument."
    val FRAG_ARG_DATE = FRAG_ARG_PREFIX + Date::class.java.name
    val FRAG_ARG_DATE_TYPE = FRAG_ARG_DATE + "Type"
    val FRAG_ARG_DATETIME_PICKER_CHOICE = FRAG_ARG_PREFIX + "datetime.picker.choice"
    val FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER = "$FRAG_ARG_PREFIX.datetime.picker.result.handler"

    @JvmOverloads
    fun createDatePickerFragment(
        date: Date,
        dateType: String,
        choice: String,
        resultHandler: DateTimePickerFragment.ResultHandler? = null
    ): DateTimePickerFragment {
        val ret = DateTimePickerFragment()
        val args = Bundle()
        args.putSerializable(FRAG_ARG_DATE, date)
        args.putSerializable(FRAG_ARG_DATE_TYPE, dateType)
        args.putSerializable(FRAG_ARG_DATETIME_PICKER_CHOICE, choice)
        args.putSerializable(FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER, resultHandler)
        ret.arguments = args
        return ret
    }

}