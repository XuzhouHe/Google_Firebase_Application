package com.example.cse438.cse438_assignment4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_tutorial1.*
import kotlinx.android.synthetic.main.activity_tutorial1.exit_button
import kotlinx.android.synthetic.main.activity_tutorial1.next_button
import kotlinx.android.synthetic.main.activity_tutorial2.*

class TutorialActivity7 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial7)
    }

    override fun onStart() {
        super.onStart()
        exit_button.setOnClickListener(){
            startActivity(Intent(this,  MainActivity::class.java))
        }
        previous_button.setOnClickListener(){
            startActivity(Intent(this, TutorialActivity6::class.java))
        }


    }

}