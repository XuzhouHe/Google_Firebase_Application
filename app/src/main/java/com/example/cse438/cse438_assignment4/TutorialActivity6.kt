package com.example.cse438.cse438_assignment4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_tutorial1.*
import kotlinx.android.synthetic.main.activity_tutorial1.exit_button
import kotlinx.android.synthetic.main.activity_tutorial1.next_button
import kotlinx.android.synthetic.main.activity_tutorial2.*
import kotlinx.android.synthetic.main.activity_tutorial2.previous_button
import kotlinx.android.synthetic.main.activity_tutorial6.*

class TutorialActivity6 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial6)
    }

    override fun onStart() {
        super.onStart()

        next_button.setOnClickListener(){
            startActivity(Intent(this, TutorialActivity7::class.java))
        }

        exit_button.setOnClickListener(){
            startActivity(Intent(this,  MainActivity::class.java))
        }
        previous_button.setOnClickListener(){
            startActivity(Intent(this, TutorialActivity5::class.java))
        }


    }

}