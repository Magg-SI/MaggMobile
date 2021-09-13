package pl.tysia.maggstone.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.tysia.maggstone.data.dao.*
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.QueueItem
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.LoggedInUser


@Database(entities = [Ware::class, Contractor::class, Error::class, QueueItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun waresDao(): WaresDAO
    abstract fun contractorsDao(): ContractorsDAO
    abstract fun errorsDAO(): ErrorsDAO
    abstract fun queueDAO(): QueueDAO
}