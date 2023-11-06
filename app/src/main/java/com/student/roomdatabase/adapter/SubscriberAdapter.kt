package com.student.roomdatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.roomdatabase.databinding.SubscriberListBinding
import com.student.roomdatabase.generated.callback.OnClickListener
import com.student.roomdatabase.room.Subscriber

class SubscriberAdapter(private val clickListener:(Subscriber)->Unit)
    :RecyclerView.Adapter<SubscriberViewHolder>()

{
    private val subscribers = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SubscriberListBinding.inflate(layoutInflater,parent,false)
        return SubscriberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscribers[position],clickListener)
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

    fun setList(subscribers: List<Subscriber>){
        this.subscribers.clear()
        this.subscribers.addAll(subscribers)
    }
}

class SubscriberViewHolder(private val binding:SubscriberListBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber,clickListener:(Subscriber)->Unit){

        binding.name.text = subscriber.subscriberName
        binding.email.text = subscriber.subscriberEmail
        binding.cardView.setOnClickListener{
            clickListener(subscriber)
        }

    }

}