package com.example.mohmoh.apodgallery.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ApodDAO {
//    @Query("SELECT * FROM apod")
//    fun fetchAllApods(): List<Apod>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun saveAllApods(apods: List<Apod>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg apods: Apod): List<Long>


    @Query("SELECT * FROM apod ORDER BY date_apod ASC")
    suspend fun getAllAPODs(): List<Apod>

    @Query("SELECT * FROM apod WHERE date_apod = :date_apod")
    suspend fun getApod(date_apod: String): Apod

    @Query("DELETE FROM apod")
    suspend fun deleteAllApods()

}