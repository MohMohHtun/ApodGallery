package com.example.mohmoh.apodgallery.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "apod")
data class Apod(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date_apod")
    @SerializedName("date")
    val dateApod: String,

    @ColumnInfo(name = "copyright")
    @SerializedName("copyright")
    val copyright: String?,

    @ColumnInfo(name = "explanation")
    @SerializedName("explanation")
    val explanation: String?,


    @ColumnInfo(name = "hdurl")
    @SerializedName("hdurl")
    val hdurl: String?,

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    val media_type: String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url: String?
)

data class ApodPlatte(var color: Int)