package pl.tysia.maggstone.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Error

@Dao
interface ErrorsDAO {
    @Query("SELECT * FROM error")
    fun getAll(): LiveData<List<Error>>

    @Query("SELECT * FROM error WHERE source = (:source)")
    fun getFromSource(source : String): LiveData<List<Error>>

    @Query("SELECT * FROM error WHERE type = (:type)")
    fun getByType(type : Int): List<Error>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(error: Error) : Long

    @Query("SELECT COUNT(source) FROM error")
    fun getNumber() : Int

    @Query("SELECT COUNT(source) FROM error")
    fun getObservableNumber() : LiveData<Int>

    @Query("DELETE FROM error WHERE source = (:source)")
    fun clearSource(source : String)
}