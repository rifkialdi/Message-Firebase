package com.example.message_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.message_firebase.adapter.MessageAdapter
import com.example.message_firebase.databinding.ActivityChatBinding
import com.example.message_firebase.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var fRealtime: DatabaseReference

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String

    private lateinit var dataChatting: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /* Mengambil data dari HomeUserActivity */
        val getName = intent.getStringExtra("name")
        supportActionBar?.title = getName
        /* variable untuk membuat sender, receiver room realtime firebase */
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        /* inisialisasi firebase realtime */
        fRealtime = Firebase.database.reference

        /* inisialisasi variabel untuk membuat room di realtime database */
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        binding.idtvSend.setOnClickListener {
            val messege = binding.idedtMessage.text.toString()
            val messageObject = Message(messege, senderUid!!)

            if (messege.isNotEmpty()) {
                fRealtime.child("Chats").child(senderRoom).child("messages").push().setValue(messageObject)
                    .addOnCompleteListener {
                        fRealtime.child("Chats").child(receiverRoom).child("messages").push().setValue(messageObject)
                    }
                binding.idedtMessage.text.clear()
            } else {

            }
        }

        dataChatting = arrayListOf()
        val messageAdapter = MessageAdapter(dataChatting)
        binding.idrvChat.adapter = messageAdapter

        fRealtime.child("Chats").child(senderRoom).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataChatting.clear()
                for (postSnapshoot in snapshot.children) {
                    val message = postSnapshoot.getValue(Message::class.java)
                    dataChatting.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
                /* agar chattingan nya selalu scrool ke bawah (yang terbaru)  */
                binding.idrvChat.scrollToPosition(dataChatting.size-1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        /* logging here */
        val loginSekarang = Firebase.auth.currentUser?.uid
        Log.e("testing", "$loginSekarang" )
    }
}