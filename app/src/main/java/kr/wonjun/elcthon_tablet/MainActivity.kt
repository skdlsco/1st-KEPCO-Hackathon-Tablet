package kr.wonjun.elcthon_tablet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)


        val thread = Thread()
        setTimeNow()
        val handler = Handler()
        val setTimer = Timer()
        setTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    setTimeNow()
                }
            }
        }, 0, 60000)
    }

    fun setTimeNow() {
        val date = Date()
        val hour = if (date.hours >= 13)
            date.hours - 12
        else date.hours

        val AmPm = if (date.hours >= 13)
            "Pm"
        else "Am"
        timeNow.text = "${hour} : ${date.minutes} $AmPm"
    }
}
