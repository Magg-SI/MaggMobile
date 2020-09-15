package pl.tysia.maggstone.data

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Ware

@Dao
interface WaresDAO {
    @Query("SELECT * FROM ware")
    fun getAll(): LiveData<List<Ware>>


    @Query("SELECT * FROM ware WHERE id = (:id)")
    fun findByID(id : Int): Ware

    @Insert
    fun insertAll(wares: List<Ware>)

    @Delete
    fun delete(ware: Ware)

    @Update
    fun update(ware: Ware)

    @Query("DELETE FROM ware")
    fun deleteAll()

    @Transaction
    fun refillTable(wares: List<Ware>){
        deleteAll()
        insertAll(wares)
    }
}