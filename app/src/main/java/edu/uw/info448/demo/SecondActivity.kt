package edu.uw.info448.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //action bar "back"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        val launchingIntent = intent
//        val extras = launchingIntent.extras
//        val message = extras!!.getString(MainActivity.EXAMPLE_MESSAGE_KEY)
//
//        findViewById<TextView>(R.id.txt_second).text = message

    }
}