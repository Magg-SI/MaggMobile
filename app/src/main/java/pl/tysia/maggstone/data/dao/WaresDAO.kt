package pl.tysia.maggstone.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.tysia.maggstone.data.model.Ware

@Dao
interface WaresDAO {
    @Query("SELECT max(counter) from ware")
    fun getMaxCounter() : Int

    @Query("SELECT * FROM ware")
    fun getAll(): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '' OR  ware.hoseType = NULL")
    fun getAllNonHose(): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '${Ware.HOSE_TYPE_CORD}'")
    fun getAllCords(): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '${Ware.HOSE_TYPE_TIP}'")
    fun getAllTips(): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '${Ware.HOSE_TYPE_TIP}' AND ware.hoseFi = :fi")
    fun getTipsFor(fi : String): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '${Ware.HOSE_TYPE_SLEEVE}'")
    fun getAllSleeves(): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE ware.hoseType = '${Ware.HOSE_TYPE_SLEEVE}' AND ware.hoseFi = :fi AND hoseIdx LIKE '%'||:hoseIdx||'%' ")
    fun getSleevesFor(fi : String, hoseIdx : String): LiveData<List<Ware>>

    @Query("SELECT * FROM ware WHERE id = :id")
    fun findByID(id : Int): Ware

    @Query("SELECT * FROM ware WHERE `index` = :idx")
    fun findByIndex(idx : String): Ware?

    @Query("SELECT * FROM ware WHERE `qrCode` = :qr")
    fun findByQr(qr : String): Ware?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(wares: List<Ware>) : List<Long>

    @Delete
    fun delete(ware: Ware)

    @Update
    fun update(ware: Ware)

    @Update
    fun updateAll(ware: List<Ware>)

    @Query("DELETE FROM ware")
    fun deleteAll()

    @Transaction
    fun refillTable(wares: List<Ware>){
        deleteAll()
        insertAll(wares)
    }
}