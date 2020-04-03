package com.example.cse438.cse438_assignment4.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_trending.*

class TrendingFragment: Fragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.cse438.cse438_assignment4.R.layout.fragment_trending, container, false)



    }

    override fun onStart() {
        super.onStart()
        var image = trackimage
        clear_button.setOnClickListener(){
        }
    }
}