package com.example.cse438.cse438_assignment4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)
//
        signupButton.setOnClickListener{
            signup()
        }

    }

    private fun signup(){
        if(signupE.text.toString().isEmpty()){
            signupE.error = "Pelase enter your email."
            signupE.requestFocus()
            return
        }
        if(signupP1.text.toString().isEmpty()){
            signupP1.error = "Pelase enter your password."
            signupP1.requestFocus()
            return
        }
        if(signupP2.text.toString().isEmpty()){
            signupP2.error = "Pelase confirm your password."
            signupP2.requestFocus()
            return
        }
        if(signupP1.text.toString()!= signupP2.text.toString()){
            signupP2.error = "Your password does not match. "
            signupP2.requestFocus()
            return
        }
        if(name.text.toString().isEmpty()){
            name.error = "Please Enter Your Name "
            name.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(signupE.text.toString(), signupP1.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val thisUser = User(signupE.text.toString(),name.text.toString(),1000,0,0,0);
                    db.collection("Users1")
                        .add(thisUser)
                        .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                            Toast.makeText(this, "User created", Toast.LENGTH_LONG)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        })
                        .addOnFailureListener(OnFailureListener { e ->
                            Toast.makeText(this, "Failed to insert data!", Toast.LENGTH_LONG)
                        })
                    startActivity(Intent(this, MainActivity::class.java))


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed, try again later",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }

}