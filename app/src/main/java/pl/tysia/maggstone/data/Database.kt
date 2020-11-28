package pl.tysia.maggstone.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware

@Database(entities = [Ware::class, Contractor::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun waresDao(): WaresDAO
    abstract fun contractorsDao(): ContractorsDAO
}