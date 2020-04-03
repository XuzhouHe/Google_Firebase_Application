package com.example.cse438.cse438_assignment4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderBoardActivity : AppCompatActivity(){

    lateinit var db : FirebaseFirestore

    var playerList : ArrayList<User> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)



    }

    override fun onStart() {
        super.onStart()

        cancel.setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.playerChipsplaylist)
        val adapter = LeaderAdapter(playerList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        db.collection("Users1")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                if (task.isSuccessful) {
                    playerList.clear()
                    for (document in task.result!!) {
                        playerList.add(
                            User(
                                document.get("userEmail").toString(),
                                document.get("userName").toString(),
                                document.get("userChips").toString().toInt(),
                                document.get("wins").toString().toInt(),
                                document.get("losts").toString().toInt(),
                                document.get("rank").toString().toInt()
                            )
                        )
                    }
                    playerList.sortByDescending { it.userChips }
                    for(i in 0 until playerList.size ){
                        playerList[i].rank = i + 1
                        System.out.println("-----------------------------------------------" + playerList[i].rank)
                        System.out.println("-----------------------------------------------" + i)


                    }
                    System.out.println("-----------------------------------------------" + playerList[0].rank)
                    adapter.notifyDataSetChanged()
                } else {
                    println("failed to get data")
                }
            })


    }
}