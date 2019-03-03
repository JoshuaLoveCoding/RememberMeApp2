package edu.gwu.rememberme2

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

abstract class AbstractDialogFragment : DialogFragment() {

    protected abstract fun processFragmentArguments()

    abstract override fun onCreateDialog(savedInstanceState: Bundle): Dialog

    protected abstract fun saveInstanceState(outState: Bundle)

    protected abstract fun restoreInstanceState(savedInstanceState: Bundle)

    override fun onSaveInstanceState(outState: Bundle) {
        saveInstanceState(outState)
    }

}