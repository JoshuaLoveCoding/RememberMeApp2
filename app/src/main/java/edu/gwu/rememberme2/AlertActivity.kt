package edu.gwu.rememberme2

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_face.view.*

class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
    }

    fun phoneButtonPressed(barpet: MenuItem) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_face_phone, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnOk.setOnClickListener {
            mAlertDialog.dismiss()
            val phone = mDialogView.editText.text.toString()
            var t = findViewById(R.id.textView) as TextView
            t.text = getString(R.string.phone_number, phone)
        }

        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

    fun messageButtonPressed(barpet: MenuItem) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_face, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnOk.setOnClickListener {
            mAlertDialog.dismiss()
            val message = mDialogView.editText.text.toString()
            var t2 = findViewById(R.id.textView2) as TextView
            t2.text = getString(R.string.alternate_phone_number, message)
        }

        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }
}
