package pl.tysia.maggstone.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Availability(@SerializedName("magazyn")
                        var warehouse : String,
                        @SerializedName("ilosc")
                        var quantity : Double
                        )