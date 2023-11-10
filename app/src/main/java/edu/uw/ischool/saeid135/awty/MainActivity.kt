package edu.uw.ischool.saeid135.awty

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Intent
import android.app.Application
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.widget.Toast


const val ALARM_ACTION = "edu.uw.ischool.saeid135.ALARM"

class MainActivity : ComponentActivity() {
    private lateinit var btn : Button
    private lateinit var message : EditText
    private lateinit var phoneNumber : EditText
    private lateinit var minutes : EditText
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    var receiver : BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message = findViewById(R.id.message)
        phoneNumber = findViewById(R.id.phoneNumber)
        minutes = findViewById(R.id.minutes)
        btn = findViewById(R.id.btn)

        btn.setOnClickListener {
            if (btn.text == "Start") {
                if (message.text.toString() == "" ||
                    phoneNumber.text.toString() == "" ||
                    minutes.text.toString() == "") {
                    Toast.makeText(this, "All fields haven't been filled yet", Toast.LENGTH_SHORT).show()

                }
                else if (minutes.text.toString() == "0"){
                    Toast.makeText(this, "Minutes can't be less than 1", Toast.LENGTH_SHORT).show()

                }
                else {
                    thereYetAlarm()
                    btn.text = "Stop"
                }
            }
            else if (btn.text == "Stop"){
                alarmManager.cancel(pendingIntent)
                btn.text = "Start"
            }
             }

    }

    fun thereYetAlarm() {
        val activityThis = this

        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Toast.makeText(activityThis, "${phoneNumber.text} ${message.text}", Toast.LENGTH_SHORT).show()
                }
            }
            val filter = IntentFilter(ALARM_ACTION)
            registerReceiver(receiver, filter)
        }

        // Create the PendingIntent
        val intent = Intent(ALARM_ACTION)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Get the Alarm Manager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            minutes.text.toString().toLong() * 60000,
            pendingIntent)
    }
}
