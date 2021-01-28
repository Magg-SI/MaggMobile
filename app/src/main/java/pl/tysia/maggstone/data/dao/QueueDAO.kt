package pl.tysia.maggstone.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.QueueItem

@Dao
interface QueueDAO {
    @Query("SELECT * FROM queueitem")
    fun getAll(): LiveData<List<QueueItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(items : List<QueueItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(item : QueueItem)

    @Delete
    fun delete(item : QueueItem)

}