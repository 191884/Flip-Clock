package com.yo.flipclock

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yo.flipclock.databinding.ActivityClockBinding
import java.text.SimpleDateFormat
import java.util.*


class ClockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myActivity = this@ClockActivity

        val sm: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var list: MutableList<Sensor> = sm.getSensorList(Sensor.TYPE_ACCELEROMETER)
        var se = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                var values: FloatArray? = sensorEvent?.values
                val x: Float? = values?.get(0)
                val y: Float? = values?.get(1)
                val z: Float? = values?.get(2)
                if(y!! < 0.0){
                    myActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
                if(y>0) {
                    myActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
        }
        sm.registerListener(se, list[0], SensorManager.SENSOR_DELAY_NORMAL)

        val displayMetrics = DisplayMetrics()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        binding.hourView.width = width - width/6
        binding.hourView.height = width - width/6
        binding.minView.width = width - width/6
        binding.minView.height = width - width/6

        binding.hourView.textSize = (height/2)/4.toFloat()
        binding.minView.textSize = (height/2)/4.toFloat()

        blink()

    }

    private fun blink() {
        val handler = Handler()
        Thread {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
                val minuteFormat = SimpleDateFormat("mm", Locale.getDefault())
                val secondFormat = SimpleDateFormat("ss", Locale.getDefault())
                val hour = hourFormat.format(Date())
                val minute = minuteFormat.format(Date())
                val second = secondFormat.format(Date()).toString()
                if(second =="00") binding.minView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.flip_point_from_middle))
                binding.hourView.text = hour
                binding.minView.text = minute
//                if(hour> 12.toString()) binding.AmPmView.text = "PM"
//                binding.SecView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.flip_point_from_middle))
                blink()
            }
        }.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this@ClockActivity, "Landscape Mode", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this@ClockActivity, "Portrait Mode", Toast.LENGTH_SHORT).show()
        }
    }
}