package com.example.cse438.cse438_assignment4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_tutorial1.*

class TutorialActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial1)
    }

    override fun onStart() {
        super.onStart()
        next_button.setOnClickListener(){
            startActivity(Intent(this,  TutorialActivity2::class.java))
        }
        exit_button.setOnClickListener(){
            startActivity(Intent(this,  MainActivity::class.java))
        }


    }

}