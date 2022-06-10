package com.example.message_firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.message_firebase.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Silahkan Masuk"
        fAuth = Firebase.auth

        binding.idtvDaftar.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.idbtnSignin.setOnClickListener {
            val email = binding.idedtEmail.text.toString()
            val password = binding.idedtPassword.text.toString()

            var check = true
            if (email.isEmpty()) {
                check = false
                binding.idedtEmail.error = "Silahkan Isikan Email anda"
                binding.idedtEmail.requestFocus()
            }
            if (password.isEmpty()) {
                check = false
                binding.idedtPassword.error = "Silahkan Isikan Password anda"
                binding.idedtPassword.requestFocus()
            }
            if (check) {
                signin(email, password)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (fAuth.currentUser != null){
            startActivity(Intent(this, HomeUserActivity::class.java))
            finish()
        }
    }

    private fun signin(email: String, password: String) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toastMessage("Berhasil Masuk")
                startActivity(Intent(this, HomeUserActivity::class.java))
                finish()
            } else {
                toastMessage("Tidak Berhasil Masuk")
            }
        }
    }

    private fun toastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}