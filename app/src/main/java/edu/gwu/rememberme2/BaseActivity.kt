package edu.gwu.rememberme2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val title = findViewById(R.id.homeTitle1) as TextView
        title.text = "Home"

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        val a = Intent(this@BaseActivity, MainActivity::class.java)
                        startActivity(a)
                    }
                    R.id.navigation_notifications -> {
                        val b = Intent(this@BaseActivity, AlertActivity::class.java)
                        startActivity(b)
                    }
                }
                return false
            }
        })
    }
}
