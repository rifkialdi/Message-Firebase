package com.example.message_firebase.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.message_firebase.databinding.ItemRvChatReceiverBinding
import com.example.message_firebase.databinding.ItemRvChatSendBinding
import com.example.message_firebase.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class ReceiverViewHolder(val bindingReceiver: ItemRvChatReceiverBinding) : RecyclerView.ViewHolder(bindingReceiver.root)
    class SendViewHolder(val bindingSend: ItemRvChatSendBinding) : RecyclerView.ViewHolder(bindingSend.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return ReceiverViewHolder(ItemRvChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return SendViewHolder(ItemRvChatSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.bindingSend.idtvSenderChat.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            holder.bindingReceiver.idtvReceiverChat.text = currentMessage.message

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (Firebase.auth.currentUser?.uid.equals(currentMessage.senderUid)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

}