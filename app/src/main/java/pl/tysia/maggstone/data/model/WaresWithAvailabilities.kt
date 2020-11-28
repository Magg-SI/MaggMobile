package pl.tysia.maggstone.data.model

import androidx.room.Embedded
import androidx.room.Relation

class WaresWithAvailabilities(
    @Embedded val ware : Ware,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    var availabilities : List<Availability>
)