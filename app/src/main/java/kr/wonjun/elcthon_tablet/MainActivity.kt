package kr.wonjun.elcthon_tablet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import kotlinx.android.synthetic.main.drawer.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val card = "0 0 FF 0 FF 0 0 0 FF C F4 D5 4B 1 1 0 4 8 4 C9 AE 58 E5 1A 0 "
    private var bt: BluetoothSPP? = null
    private var isComeIn = false
    private var isTimerStared = false
    private var usePrice = 0
    private var remainingTime: Long = 0
    private var countTimer = Timer()
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.drawer)

        bt = BluetoothSPP(this)

        val thread = Thread()
        setTimeNow()
        val setTimer = Timer()
        setTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    setTimeNow()
                }
            }
        }, 0, 60000)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        call.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
            temperatureLayout.visibility = View.GONE
            callLayout.visibility = View.VISIBLE
            phoneNumber.text = ""
        }

        temperature.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
            temperatureLayout.visibility = View.VISIBLE
            callLayout.visibility = View.GONE
        }


        bt?.setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener { data, message ->
            Log.e("asdf123", message)
            if (!isComeIn) {
                if (card.equals(message)) {
                    isComeIn = true
                }
            } else {
                if (message.equals("sex")) {

                }
            }
        })
        exit.setOnClickListener {
            bt?.send("5", false)
            Log.e("send", "send")
        }

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btn10.setOnClickListener(this)
        btn11.setOnClickListener(this)
        btn12.setOnClickListener(this)
        btn13.setOnClickListener(this)
        startCount()
    }

    private fun startCount() {
        val date = Date()
        registrationTime.text = SimpleDateFormat("yyyy-MM-dd hh:mm aa").format(date)
        usePrice += 1000
        remainingTime += 1000 * 60 * 60
        price.text = "${usePrice}₩"
        countTimer.schedule(object : TimerTask() {
            override fun run() {
                remainingTime -= 1000;
                handler.post {
                    printRemainingTime()
                }
            }

        }, 0, 1000)
    }

    fun printRemainingTime() {
        val hour = remainingTime / (1000 * 60 * 60)
        val minute = (remainingTime % (1000 * 60 * 60)) / (1000 * 60)
        val second = (remainingTime % (60000)) / 1000

        val mhour = if (hour < 10)
            "0${hour}"
        else
            "${hour}"

        val mMinute = if (minute < 10)
            "0${minute}"
        else
            "${minute}"
        val mSecond = if (second < 10)
            "0${second}"
        else
            "${second}"
        limitTime.text = mhour + ":" + mMinute + ":" + mSecond
    }

    fun reset() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn1 -> phoneNumber.text = phoneNumber.text.toString() + "1"
            R.id.btn2 -> phoneNumber.text = phoneNumber.text.toString() + "2"
            R.id.btn3 -> phoneNumber.text = phoneNumber.text.toString() + "3"
            R.id.btn4 -> phoneNumber.text = phoneNumber.text.toString() + "4"
            R.id.btn5 -> phoneNumber.text = phoneNumber.text.toString() + "5"
            R.id.btn6 -> phoneNumber.text = phoneNumber.text.toString() + "6"
            R.id.btn7 -> phoneNumber.text = phoneNumber.text.toString() + "7"
            R.id.btn8 -> phoneNumber.text = phoneNumber.text.toString() + "8"
            R.id.btn9 -> phoneNumber.text = phoneNumber.text.toString() + "9"
            R.id.btn10 -> phoneNumber.text = phoneNumber.text.toString() + "*"
            R.id.btn11 -> phoneNumber.text = phoneNumber.text.toString() + "0"
            R.id.btn12 -> phoneNumber.text = phoneNumber.text.toString() + "#"
            R.id.btn13 -> {
                val sky = Intent("android.intent.action.CALL_PRIVILEGED")
                sky.setClassName("com.skype.raider",
                        "com.skype.raider.Main")
                sky.data = Uri.parse("skype:+82" + phoneNumber.text.toString())
                startActivity(sky)
            }
        }
    }

    fun setTimeNow() {
        val date = Date()
        timeNow.text = SimpleDateFormat("hh : mm aa").format(date)
    }

    override fun onPause() {
        bt?.stopService()
        super.onPause()
    }

    public override fun onDestroy() {
        bt?.stopService()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        bt?.setupService()
        bt?.startService(BluetoothState.DEVICE_OTHER)
        bt?.autoConnect("haejuk")
    }
}
