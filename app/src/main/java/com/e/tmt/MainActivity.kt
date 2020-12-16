
package com.e.tmt

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.e.tmt.cabinet.cabinet
import com.e.tmt.memo.lamp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        buttonEffect(lampButton)
        buttonEffect(cardButton)
        buttonEffect(cabinetButton)
        buttonEffect(settingsButton)

        lampButton.setOnClickListener{
            val intent = Intent(this, lamp::class.java)
            startActivity(intent)
        }

        cardButton.setOnClickListener{
            val intent = Intent(this, cards::class.java)
            startActivity(intent)
        }

        cabinetButton.setOnClickListener{
            val intent = Intent(this, cabinet::class.java)
            startActivity(intent)
        }

    }

    private var buttonClick = AlphaAnimation(1f, 0.4f)
    private var buttonBack = AlphaAnimation(0.4f, 1f)

    @SuppressLint("ClickableViewAccessibility")
    fun buttonEffect(button: View) {
        buttonClick.duration = 200
        buttonBack.duration = 200
        button.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(buttonClick)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.startAnimation(buttonBack)
                    v.invalidate()
                }
            }
            false
        }

    }

}


