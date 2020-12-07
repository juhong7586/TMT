
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.tmt.cabinet.CabinetAdapter
import com.e.tmt.cabinet.cabinet
import com.e.tmt.memo.lamp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cabinet_list.*


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

    @SuppressLint("ClickableViewAccessibility")
fun buttonEffect(button: View) {
    button.setOnTouchListener { v, event ->
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                v.startAnimation(buttonClick)
                v.background.setColorFilter(
                    Color.parseColor("#464545"),
                    PorterDuff.Mode.SRC_ATOP
                )

                v.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                v.background.clearColorFilter()
                v.invalidate()
            }
        }
        false
    }
}

private val buttonClick = AlphaAnimation(1f, 0.8f)
}


