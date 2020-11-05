
package com.e.tmt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

}


