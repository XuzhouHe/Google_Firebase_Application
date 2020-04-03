package com.example.cse438.cse438_assignment4

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.segment_leader, parent, false)) {
    private val playerview: TextView


    init {
        playerview = itemView.findViewById(R.id.leaderelement)

    }

    fun bind(user: User) {
        playerview.text ="      " + user.rank.toString() + ". " + user.userName + " with " + user.userChips.toString() + " chips"
    }

}

class LeaderAdapter (private val list: ArrayList<User>)
    : RecyclerView.Adapter<PlayerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlayerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val brewery: User = list[position]
        holder.bind(brewery)
    }

    override fun getItemCount(): Int = list.size

}
