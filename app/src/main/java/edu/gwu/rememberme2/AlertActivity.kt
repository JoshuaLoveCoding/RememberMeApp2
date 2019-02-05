package edu.gwu.rememberme2

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_alert.*
import kotlinx.android.synthetic.main.dialog_face.view.*

class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        setSupportActionBar(alert_toolbar)

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        val a = Intent(this@AlertActivity, MainActivity::class.java)
                        startActivity(a)
                        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)//show from left side
                    }
                    R.id.navigation_notifications -> {
                        val b = Intent(this@AlertActivity, AlertActivity::class.java)
                        startActivity(b)
                        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)//show from right side
                    }
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.alert_menu, menu)
        return true
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