package com.student.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.roomdatabase.adapter.SubscriberAdapter
import com.student.roomdatabase.databinding.ActivityMainBinding
import com.student.roomdatabase.repo.SubscriberRepository
import com.student.roomdatabase.room.Subscriber
import com.student.roomdatabase.room.SubscriberDataBase
import com.student.roomdatabase.viewmodel.SubscriberViewModel
import com.student.roomdatabase.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SubscriberViewModel
    private lateinit var adapter: SubscriberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the layout
        setContentView(binding.root) // Use root as the main layout

        val dao = SubscriberDataBase.getInstance(application).subscriberDao
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        viewModel = ViewModelProvider(this,factory)[SubscriberViewModel::class.java]
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        viewModel.message.observe(this) {
            Toast.makeText(this, it.getContentIfNotHandled(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SubscriberAdapter { selectedItem: Subscriber -> clickListner(selectedItem) }
        binding.recyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
            viewModel.subscribers.observe(this) {
                Log.i("Display", it.toString())
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
    }

    private fun clickListner(subscriber: Subscriber){
        viewModel.initUpdateAndDelete(subscriber)


    }

}

