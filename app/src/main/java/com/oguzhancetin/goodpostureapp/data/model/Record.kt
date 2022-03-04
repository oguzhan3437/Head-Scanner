package com.oguzhancetin.goodpostureapp.data.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    @ColumnInfo(name = "title")
    val title:String?,
    @ColumnInfo(name = "image_uri")
    val imageUri: String?
)
