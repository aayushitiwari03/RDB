package com.student.roomdatabase.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber(

    @PrimaryKey(autoGenerate = true)
    val subscriberId:Int,

    var subscriberName:String,

    var subscriberEmail:String

)
