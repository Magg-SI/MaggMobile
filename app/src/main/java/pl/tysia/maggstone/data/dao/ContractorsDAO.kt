package pl.tysia.maggstone.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware

@Dao
interface ContractorsDAO {
    @Query("SELECT * FROM contractor")
    fun getAll(): LiveData<List<Contractor>>

    @Query("SELECT max(counter) from contractor")
    fun getMaxCounter() : Int

    @Query("SELECT * FROM contractor WHERE id = (:id)")
    fun findByID(id : Int): Contractor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(contractors: List<Contractor>) : List<Long>

    @Delete
    fun delete(contractor: Contractor)

    @Update
    fun update(contractor: Contractor)


    @Update
    fun updateAll(contractor: List<Contractor>)

    @Query("DELETE FROM contractor")
    fun deleteAll()

    @Transaction
    fun refillTable(contractors: List<Contractor>){
        deleteAll()
        insertAll(contractors)
    }
}