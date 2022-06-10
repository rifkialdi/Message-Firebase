package com.example.message_firebase.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.message_firebase.ChatActivity
import com.example.message_firebase.databinding.ItemRvHomeuserBinding
import com.example.message_firebase.model.Users

class HomeUserAdapter(val listData: ArrayList<Users>, val context: Context) : RecyclerView.Adapter<HomeUserAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRvHomeuserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvHomeuserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = listData[position]
        holder.binding.idtvItemuser.text = currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.userUid)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}