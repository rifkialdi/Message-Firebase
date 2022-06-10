package com.example.message_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.message_firebase.adapter.HomeUserAdapter
import com.example.message_firebase.databinding.ActivityHomeBinding
import com.example.message_firebase.model.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataUsers: ArrayList<Users>
    private lateinit var fRealtime: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""

        binding.idtvLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }

        binding.idrvUsers.layoutManager = LinearLayoutManager(this)
        binding.idrvUsers.setHasFixedSize(true)
        dataUsers = arrayListOf()

        getUsers()
    }

    fun getUsers() {
        fRealtime = Firebase.database.getReference("Users")
        /* Untuk Mengambil semua data di realtime firebase dan di tampilkan dengan recycler view */
        fRealtime.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    dataUsers.clear()
                    for (userSnapshoot in snapshot.children) {
                        val user = userSnapshoot.getValue(Users::class.java)
                        if (Firebase.auth.uid != user!!.userUid) {
                            dataUsers.add(user!!)
                        }
                    }
                    binding.idrvUsers.adapter = HomeUserAdapter(dataUsers, applicationContext)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        /* Untuk Mengambil data di Realtime Database */
        fRealtime.child(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value
                supportActionBar?.title = name.toString()
            } else {
                Toast.makeText(this, "Tidak ada", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}