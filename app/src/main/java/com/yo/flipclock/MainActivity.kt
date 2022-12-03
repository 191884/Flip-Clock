package com.yo.flipclock

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.yo.flipclock.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                var hour = hourFormat.format(Date())
                var minute = minuteFormat.format(Date())
                var second = secondFormat.format(Date()).toString()
                if(second =="00") binding.MinView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.flip_point_from_middle))
                binding.HourView.text = hour
                binding.MinView.text = minute
                blink()
            }
        }.start()
    }
}