package com.example.message_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.message_firebase.databinding.ActivitySignupBinding
import com.example.message_firebase.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fRealtime: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Silahkan Daftar"

        fAuth = Firebase.auth
        fRealtime = Firebase.database.reference

        binding.idbtnSignup.setOnClickListener {
            val name = binding.idedtName.text.toString()
            val email = binding.idedtEmail.text.toString()
            val password = binding.idedtPassword.text.toString()

            var check = true
            if (name.isEmpty()) {
                check = false
                binding.idedtName.error = "Isikan Nama"
                binding.idedtName.requestFocus()
            }
            if (email.isEmpty()) {
                check = false
                binding.idedtEmail.error = "Isikan Email"
                binding.idedtEmail.requestFocus()
            }
            if (password.isEmpty()) {
                check = false
                binding.idedtPassword.error = "Isikan Password"
                binding.idedtPassword.requestFocus()
            }
            if (check) {
                addAuth(name, email, password)
            }
        }

    }

    private fun addAuth(name: String, email: String, password: String) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                addUserToRealtime(name, email, password)
                toastMessage("Berhasil Daftar")
                finish()
                startActivity(Intent(this, HomeUserActivity::class.java))
            } else {
                toastMessage("Daftar Tidak Berhasil")
            }
        }
    }

    private fun addUserToRealtime(name: String, email: String, password: String) {
        val userUid = fAuth.uid.toString() /* Mengambil user uid dari firebase authentication  */
        val dataUser = Users(name, email, password, userUid)
        fRealtime.child("Users").child(userUid).setValue(dataUser)
    }

    private fun toastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}