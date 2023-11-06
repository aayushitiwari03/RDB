package com.student.roomdatabase.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.roomdatabase.Event
import com.student.roomdatabase.repo.SubscriberRepository
import com.student.roomdatabase.room.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(){

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

     val saveOrUpdateButtonText = MutableLiveData<String>()
     val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: MutableLiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate(){

        if (inputName.value == null) {
            statusMessage.value = Event("Please Enter Subscriber's Name")
        } else if (inputEmail.value == null){
            statusMessage.value = Event("Please Enter Subscriber's Email")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please Enter Correct Email Address")
        }else{
            if (isUpdateOrDelete){
                subscriberToUpdateOrDelete.subscriberName = inputName.value!!
                subscriberToUpdateOrDelete.subscriberEmail = inputEmail.value!!
                updateSubscriber(subscriberToUpdateOrDelete)

            }else{
                val name = inputName.value
                val email = inputEmail.value
                if (name != null && email != null) {
                    insertSubscriber(Subscriber(0,name,email))
                }
                inputName.value = ""
                inputEmail.value = ""
            }
        }

    }

    fun clearAllOrDelete(){
        if (isUpdateOrDelete){
            deleteSubscriber(subscriberToUpdateOrDelete)}
        else{
        deleteAllSubscriber()
        }
    }

    private fun insertSubscriber(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO) {
            val newRowId = repository.insert(subscriber)
            withContext(Dispatchers.Main){
                if (newRowId > -1){
                    statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
                }else{
                    statusMessage.value = Event("Error Occurred")
                }
            }
        }
    }

    private fun updateSubscriber(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfRow = repository.update(subscriber)
            withContext(Dispatchers.Main){
                if (numberOfRow > 0){
                    inputName.value = ""
                    inputEmail.value = ""
                    isUpdateOrDelete = true
                    saveOrUpdateButtonText.value = "Save"
                    clearAllOrDeleteButtonText.value = "Clear All"
                    statusMessage.value = Event("Subscriber Updated Successfully $numberOfRow")
                }else{
                    statusMessage.value = Event("Error Occurred")
                }
            }
        }
    }

    private fun deleteSubscriber(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO) {
            val deletedNoOfRows = repository.delete(subscriber)
            withContext(Dispatchers.Main){
                if (deletedNoOfRows > 0){
                    inputName.value = ""
                    inputEmail.value = ""
                    isUpdateOrDelete = false
                    saveOrUpdateButtonText.value = "Save"
                    clearAllOrDeleteButtonText.value = "Clear All"
                    statusMessage.value = Event("Subscriber Deleted Successfully $deletedNoOfRows")
                }else{
                    statusMessage.value = Event("Error Occurred")
                }
            }
        }
    }

    private fun deleteAllSubscriber(){
        viewModelScope.launch(Dispatchers.IO) {
            val deletedRows = repository.deleteAll()
            withContext(Dispatchers.Main){
                if (deletedRows > 0){
                    statusMessage.value = Event("$deletedRows Subscriber Deleted Successfully")
                }else{
                    statusMessage.value = Event("Error Occurred")
                }
            }

        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.subscriberName
        inputEmail.value = subscriber.subscriberEmail
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}