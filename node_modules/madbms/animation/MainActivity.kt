package com.example.animation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)

        findViewById<Button>(R.id.fade_in).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fad_in)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.fade_out).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.zoom_in).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.zoom_out).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.slide_up).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.slide_down).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.bounce).setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            textView.startAnimation(animation)
        }

        findViewById<Button>(R.id.rotate).setOnClickListener {
            textView.animate().apply {
                duration = 1000
                rotationXBy(360f)
            }.start()
        }
    }
}

