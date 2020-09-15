package pl.tysia.maggstone.data

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware

@Dao
interface ContractorsDAO {
    @Query("SELECT * FROM contractor")
    fun getAll(): LiveData<List<Contractor>>


    @Query("SELECT * FROM contractor WHERE id = (:id)")
    fun findByID(id : Int): Contractor

    @Insert
    fun insertAll(contractors: List<Contractor>)

    @Delete
    fun delete(contractor: Contractor)

    @Update
    fun update(contractor: Contractor)

    @Query("DELETE FROM contractor")
    fun deleteAll()

    @Transaction
    fun refillTable(contractors: List<Contractor>){
        deleteAll()
        insertAll(contractors)
    }
}